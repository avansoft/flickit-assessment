package org.flickit.assessment.kit.application.service.assessmentkit.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flickit.assessment.common.application.domain.notification.NotificationCreator;
import org.flickit.assessment.common.application.domain.notification.NotificationEnvelope;
import org.flickit.assessment.kit.application.domain.AssessmentKit;
import org.flickit.assessment.kit.application.domain.User;
import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.user.LoadUserPort;
import org.flickit.assessment.kit.application.service.assessmentkit.notification.ToggleKitLikeNotificationPayload.AssessmentKitModel;
import org.flickit.assessment.kit.application.service.assessmentkit.notification.ToggleKitLikeNotificationPayload.UserModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ToggleKitLikeNotificationCreator
    implements NotificationCreator<ToggleKitLikeNotificationCmd> {

    private final LoadAssessmentKitPort loadAssessmentKitPort;
    private final LoadUserPort loadUserPort;

    @Override
    public List<NotificationEnvelope> create(ToggleKitLikeNotificationCmd cmd) {
        if (!cmd.liked()) {
            log.debug("kit unliked");
            return List.of();
        }
        AssessmentKit assessmentKit = loadAssessmentKitPort.load(cmd.kitId());
        Optional<User> user = loadUserPort.loadById(cmd.targetUserId());
        if (user.isEmpty()) {
            log.warn("assessment or user not found");
            return List.of();
        }
        return List.of(
            new NotificationEnvelope(cmd.targetUserId(), new ToggleKitLikeNotificationPayload(
                new AssessmentKitModel(assessmentKit.getId(), assessmentKit.getTitle()),
                new UserModel(user.get().getId(), user.get().getDisplayName())
            ))
        );
    }

    @Override
    public Class<ToggleKitLikeNotificationCmd> cmdClass() {
        return ToggleKitLikeNotificationCmd.class;
    }
}
