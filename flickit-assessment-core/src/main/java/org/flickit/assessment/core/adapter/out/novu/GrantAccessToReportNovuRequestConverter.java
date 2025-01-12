package org.flickit.assessment.core.adapter.out.novu;

import org.flickit.assessment.common.adapter.out.novu.AbstractNovuRequestConverter;
import org.flickit.assessment.common.application.domain.notification.NotificationPayload;
import org.flickit.assessment.core.application.service.assessment.notification.GrantAccessToReportNotificationPayload;
import org.springframework.stereotype.Component;

import static org.flickit.assessment.common.adapter.out.novu.NotificationType.GRANT_ACCESS_TO_REPORT;

@Component
public class GrantAccessToReportNovuRequestConverter extends AbstractNovuRequestConverter {
    @Override
    protected String getEventName() {
        return GRANT_ACCESS_TO_REPORT.getCode();
    }

    @Override
    public Class<? extends NotificationPayload> payloadClass() {
        return GrantAccessToReportNotificationPayload.class;
    }
}
