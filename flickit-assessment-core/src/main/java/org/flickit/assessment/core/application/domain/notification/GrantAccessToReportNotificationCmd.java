package org.flickit.assessment.core.application.domain.notification;

import org.flickit.assessment.common.application.domain.notification.NotificationCmd;
import org.flickit.assessment.core.application.domain.Assessment;
import org.flickit.assessment.core.application.domain.User;

import java.util.UUID;

public record GrantAccessToReportNotificationCmd(Assessment assessment,
                                                 User targetUser,
                                                 UUID senderId) implements NotificationCmd {
}
