package org.flickit.assessment.kit.application.service.assessmentkit.notification;

import org.flickit.assessment.common.application.domain.notification.NotificationCmd;

import java.util.UUID;

public record ToggleKitLikeNotificationCmd(UUID targetUserId,
                                           Long kitId,
                                           UUID kitLikerUserId,
                                           int likesCount,
                                           boolean liked) implements NotificationCmd {
}
