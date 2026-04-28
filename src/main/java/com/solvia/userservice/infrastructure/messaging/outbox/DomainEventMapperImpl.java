package com.solvia.userservice.infrastructure.messaging.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvia.userservice.domain.event.DomainEvent;
import com.solvia.userservice.domain.event.EventMetadata;
import com.solvia.userservice.shared.events.IntegrationEvent;

import java.util.Map;
import java.util.Objects;

public final class DomainEventMapperImpl implements DomainEventMapper {

    private final ObjectMapper objectMapper;

    public DomainEventMapperImpl(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public IntegrationEvent map(DomainEvent event) {

        Objects.requireNonNull(event, "domain event must not be null");

        EventMetadata meta = event.metadata();

        return new IntegrationEvent(
                meta.eventId().value(),
                event.eventType().value(),
                extractAggregateId(meta),
                "default",
                meta.correlationId().toString(),
                serialize(event),
                event.occurredAt(),
                Map.of()
        );
    }

    private String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new IllegalStateException("Serialization failed", e);
        }
    }

    private String extractAggregateId(EventMetadata meta) {
        return meta.actorId().value().toString();
    }
}