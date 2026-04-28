package com.solvia.userservice.infrastructure.messaging.outbox;

import com.solvia.userservice.shared.events.IntegrationEvent;
import com.solvia.userservice.application.port.out.event.EventPublisher;
import com.solvia.userservice.infrastructure.messaging.outbox.entity.OutboxEventEntity;
import com.solvia.userservice.infrastructure.messaging.outbox.repository.OutboxRepository;
import com.solvia.userservice.domain.event.DomainEvent;

import java.util.Collection;
import java.util.Objects;

public class OutboxDomainEventPublisher implements EventPublisher {

    private final OutboxRepository repository;
    private final DomainEventMapper mapper;

    public OutboxDomainEventPublisher(
        OutboxRepository repository,
        DomainEventMapper mapper
    ) {
        this.repository = Objects.requireNonNull(repository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public void publish(Collection<DomainEvent> events) {

        Objects.requireNonNull(events, "events must not be null");

        for (DomainEvent domainEvent : events) {

            IntegrationEvent integrationEvent = mapper.map(domainEvent);

            OutboxEventEntity entity =
                OutboxEventFactory.from(integrationEvent);

            repository.save(entity);
        }
    }
}
