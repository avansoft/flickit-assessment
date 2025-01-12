package org.flickit.assessment.core.application.service.assessment.notification;

import org.flickit.assessment.common.application.domain.notification.NotificationPayload;

import java.util.UUID;

public record GrantAccessToReportNotificationPayload(AssessmentModel assessment,
                                                     UserModel sender) implements NotificationPayload {

    public record AssessmentModel(UUID id, String title, long spaceId) {}

    public record UserModel(UUID id, String displayName) {}
}
