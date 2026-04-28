package com.solvia.userservice.infrastructure.messaging.outbox;

import com.solvia.userservice.shared.events.IntegrationEvent;
import com.solvia.userservice.domain.event.DomainEvent;

public interface DomainEventMapper {
    IntegrationEvent map(DomainEvent event);
}
