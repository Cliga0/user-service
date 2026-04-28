package com.solvia.userservice.infrastructure.messaging.outbox;

import com.solvia.userservice.shared.events.IntegrationEvent;
import com.solvia.userservice.infrastructure.messaging.outbox.entity.OutboxEventEntity;

public final class OutboxEventFactory {

    private OutboxEventFactory() {}

    public static OutboxEventEntity from(IntegrationEvent event) {
        return OutboxEventEntity.pending(
            "USER",
            event.aggregateId(),
            event.eventType(),
            "v1",
            event.tenantId(),
            event.traceId(),
            event.occurredAt(),
            event.payload(),
            null
        );
    }
}
