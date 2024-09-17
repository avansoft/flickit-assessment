package org.flickit.assessment.common.adapter.out.novu;

import co.novu.api.common.SubscriberRequest;
import co.novu.api.events.requests.TriggerEventRequest;
import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.notification.NotificationEnvelope;
import org.flickit.assessment.common.application.domain.notification.Tenant;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractNovuRequestConverter implements NovuRequestConverter {

    private final TenantProperties tenantProperties;

    @Override
    public TriggerEventRequest convert(NotificationEnvelope envelope) {
        var triggerEvent = new TriggerEventRequest();
        triggerEvent.setTenant(createTenant());
        triggerEvent.setName(getEventName());
        triggerEvent.setTo(createSubscriberRequest(envelope.targetUserId()));
        triggerEvent.setPayload(Map.of("data", envelope.payload(), "title", envelope.title()));
        return triggerEvent;
    }

    private Tenant createTenant() {
        return new Tenant(tenantProperties.getTenantId());
    }

    protected abstract String getEventName();

    private SubscriberRequest createSubscriberRequest(UUID targetUserId) {
        var subscriber = new SubscriberRequest();
        subscriber.setSubscriberId(targetUserId.toString());
        return subscriber;
    }
}