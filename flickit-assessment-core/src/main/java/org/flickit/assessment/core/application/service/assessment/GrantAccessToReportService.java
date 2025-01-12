package org.flickit.assessment.core.application.service.assessment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flickit.assessment.common.application.MessageBundle;
import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.application.domain.notification.SendNotification;
import org.flickit.assessment.common.application.port.out.SendEmailPort;
import org.flickit.assessment.common.config.AppSpecProperties;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceAlreadyExistsException;
import org.flickit.assessment.common.exception.ValidationException;
import org.flickit.assessment.core.application.domain.Assessment;
import org.flickit.assessment.core.application.domain.notification.GrantAccessToReportNotificationCmd;
import org.flickit.assessment.core.application.port.in.assessment.GrantAccessToReportUseCase;
import org.flickit.assessment.core.application.port.out.assessment.LoadAssessmentPort;
import org.flickit.assessment.core.application.port.out.assessmentinvite.CreateAssessmentInvitePort;
import org.flickit.assessment.core.application.port.out.assessmentuserrole.GrantUserAssessmentRolePort;
import org.flickit.assessment.core.application.port.out.assessmentuserrole.LoadUserRoleForAssessmentPort;
import org.flickit.assessment.core.application.port.out.space.CreateSpaceInvitePort;
import org.flickit.assessment.core.application.port.out.spaceuseraccess.CheckSpaceAccessPort;
import org.flickit.assessment.core.application.port.out.spaceuseraccess.CreateAssessmentSpaceUserAccessPort;
import org.flickit.assessment.core.application.port.out.user.LoadUserPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.GRANT_ACCESS_TO_REPORT;
import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.VIEW_GRAPHICAL_REPORT;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.common.error.ErrorMessageKey.INVITE_TO_REGISTER_EMAIL_SUBJECT;
import static org.flickit.assessment.core.application.domain.AssessmentUserRole.REPORT_VIEWER;
import static org.flickit.assessment.core.common.ErrorMessageKey.GRANT_ACCESS_TO_REPORT_NOT_ALLOWED_CONTACT_ASSESSMENT_MANAGER;
import static org.flickit.assessment.core.common.ErrorMessageKey.GRANT_ACCESS_TO_REPORT_USER_ALREADY_GRANTED;
import static org.flickit.assessment.core.common.MessageKey.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GrantAccessToReportService implements GrantAccessToReportUseCase {

    private final AssessmentAccessChecker assessmentAccessChecker;
    private final LoadAssessmentPort loadAssessmentPort;
    private final LoadUserPort loadUserPort;
    private final GrantUserAssessmentRolePort grantUserAssessmentRolePort;
    private final LoadUserRoleForAssessmentPort loadUserRoleForAssessmentPort;
    private final CreateSpaceInvitePort createSpaceInvitePort;
    private final CreateAssessmentInvitePort createAssessmentInvitePort;
    private final AppSpecProperties appSpecProperties;
    private final SendEmailPort sendEmailPort;
    private final CheckSpaceAccessPort checkSpaceAccessPort;
    private final CreateAssessmentSpaceUserAccessPort createAssessmentSpaceUserAccessPort;

    private static final Duration EXPIRY_DURATION = Duration.ofDays(7);

    @Override
    @SendNotification
    public Result grantAccessToReport(Param param) {
        if (!assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), GRANT_ACCESS_TO_REPORT))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        var userOptional = loadUserPort.loadByEmail(param.getEmail());
        Assessment assessment = loadAssessmentPort.getAssessmentById(param.getAssessmentId()).orElseThrow();
        var creationTime = LocalDateTime.now();

        if (userOptional.isPresent()) {
            var user = userOptional.get();
            var roleOptional = loadUserRoleForAssessmentPort.load(param.getAssessmentId(), user.getId());
            if (roleOptional.isPresent()) {
                if (!assessmentAccessChecker.isAuthorized(param.getAssessmentId(), user.getId(), VIEW_GRAPHICAL_REPORT))
                    throw new ValidationException(GRANT_ACCESS_TO_REPORT_NOT_ALLOWED_CONTACT_ASSESSMENT_MANAGER);
                else
                    throw new ResourceAlreadyExistsException(GRANT_ACCESS_TO_REPORT_USER_ALREADY_GRANTED);
            } else {
                if (!checkSpaceAccessPort.checkIsMember(assessment.getSpace().getId(), user.getId())) {
                    var createSpaceAccessParam = new CreateAssessmentSpaceUserAccessPort.Param(
                        assessment.getId(), user.getId(), param.getCurrentUserId(), creationTime);
                    createAssessmentSpaceUserAccessPort.persist(createSpaceAccessParam);
                }
                grantUserAssessmentRolePort.persist(param.getAssessmentId(), user.getId(), REPORT_VIEWER.getId());
            }

            return new Result(new GrantAccessToReportNotificationCmd(assessment, user, param.getCurrentUserId()));

        } else {
            var expirationTime = creationTime.plusDays(EXPIRY_DURATION.toDays());
            createSpaceInvitePort.persist(toCreateSpaceInviteParam(assessment.getSpace().getId(), param, expirationTime, creationTime));
            createAssessmentInvitePort.persist(toCreateAssessmentInviteParam(param, expirationTime, creationTime));
            sendInviteEmail(param.getEmail(), assessment);
        }

        return null;
    }

    CreateSpaceInvitePort.Param toCreateSpaceInviteParam(long spaceId,
                                                         Param param,
                                                         LocalDateTime expirationTime,
                                                         LocalDateTime creationTime) {
        return new CreateSpaceInvitePort.Param(spaceId,
            param.getEmail(),
            expirationTime,
            creationTime,
            param.getCurrentUserId());
    }

    CreateAssessmentInvitePort.Param toCreateAssessmentInviteParam(Param param,
                                                                   LocalDateTime expirationTime,
                                                                   LocalDateTime creationTime) {
        return new CreateAssessmentInvitePort.Param(param.getAssessmentId(),
            param.getEmail(),
            REPORT_VIEWER.getId(),
            expirationTime,
            creationTime,
            param.getCurrentUserId());
    }

    private void sendInviteEmail(String sendTo, Assessment assessment) {
        String subject = MessageBundle.message(GRANT_ACCESS_TO_REPORT_INVITE_TO_REGISTER_EMAIL_SUBJECT, assessment.getTitle());
        String body = generateEmailBody(assessment);
        log.debug("Sending invite email to [{}]", sendTo);
        sendEmailPort.send(sendTo, subject, body);
    }

    private String generateEmailBody(Assessment assessment) {
        String reportLink = MessageFormat.format("{0}/{1}/assessments/{2}/graphical-report",
            appSpecProperties.getHost(),
            assessment.getSpace().getId(),
            assessment.getId());
        if (appSpecProperties.getSupportEmail() == null || appSpecProperties.getSupportEmail().isBlank())
            return MessageBundle.message(GRANT_ACCESS_TO_REPORT_INVITE_TO_REGISTER_EMAIL_BODY_WITHOUT_SUPPORT_EMAIL,
                appSpecProperties.getHost(),
                appSpecProperties.getName(),
                assessment.getTitle(),
                reportLink);
        return MessageBundle.message(GRANT_ACCESS_TO_REPORT_INVITE_TO_REGISTER_EMAIL_BODY,
            appSpecProperties.getHost(),
            appSpecProperties.getName(),
            appSpecProperties.getSupportEmail(),
            assessment.getTitle(),
            reportLink);
    }
}
