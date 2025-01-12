package org.flickit.assessment.core.application.service.assessment.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flickit.assessment.common.application.MessageBundle;
import org.flickit.assessment.common.application.domain.notification.NotificationCreator;
import org.flickit.assessment.common.application.domain.notification.NotificationEnvelope;
import org.flickit.assessment.common.application.domain.notification.NotificationEnvelope.User;
import org.flickit.assessment.core.application.domain.notification.GrantAccessToReportNotificationCmd;
import org.flickit.assessment.core.application.service.assessment.notification.GrantAccessToReportNotificationPayload.AssessmentModel;
import org.flickit.assessment.core.application.service.assessment.notification.GrantAccessToReportNotificationPayload.UserModel;
import org.flickit.assessment.core.application.port.out.user.LoadUserPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.flickit.assessment.core.common.MessageKey.NOTIFICATION_TITLE_GRANT_ACCESS_TO_REPORT;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GrantAccessToReportNotificationCreator implements NotificationCreator<GrantAccessToReportNotificationCmd> {

    private final LoadUserPort loadUserPort;

    @Override
    public List<NotificationEnvelope> create(GrantAccessToReportNotificationCmd cmd) {
        var senderOptional = loadUserPort.loadById(cmd.senderId());
        if (senderOptional.isEmpty()) {
            log.warn("user not found");
            return List.of();
        }
        var title = MessageBundle.message(NOTIFICATION_TITLE_GRANT_ACCESS_TO_REPORT);
        var payload = new GrantAccessToReportNotificationPayload(
            new AssessmentModel(cmd.assessment().getId(),
                cmd.assessment().getTitle(),
                cmd.assessment().getSpace().getId()),
            new UserModel(senderOptional.get().getId(), senderOptional.get().getDisplayName())
        );
        return List.of(new NotificationEnvelope(new User(cmd.targetUser().getId(), cmd.targetUser().getEmail()), title, payload));
    }

    @Override
    public Class<GrantAccessToReportNotificationCmd> cmdClass() {
        return GrantAccessToReportNotificationCmd.class;
    }
}
