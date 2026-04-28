package com.solvia.userservice.infrastructure.messaging.outbox.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvia.userservice.infrastructure.messaging.outbox.entity.OutboxEventEntity;
import com.solvia.userservice.shared.events.IntegrationEvent;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class OutboxEventMapper {

    private final ObjectMapper objectMapper;

    public OutboxEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // ---------------------------------------------------------------------
    // ENTITY -> INTEGRATION EVENT
    // ---------------------------------------------------------------------

    public IntegrationEvent toIntegrationEvent(OutboxEventEntity entity) {

        Objects.requireNonNull(entity, "entity must not be null");

        Map<String, String> headers = deserializeHeaders(entity.headersJson());

        return new IntegrationEvent(
                entity.id(),
                entity.eventType(),
                entity.aggregateId(),
                entity.tenantId(),
                entity.traceId(),
                entity.payload(),
                entity.occurredAt(),
                deserializeHeaders(entity.headersJson())
        );
    }

    // ---------------------------------------------------------------------
    // JSON DESERIALIZATION
    // ---------------------------------------------------------------------

    private Map<String, String> deserializeHeaders(String headersJson) {

        if (headersJson == null || headersJson.isBlank()) {
            return Map.of();
        }

        try {
            return objectMapper.readValue(
                    headersJson,
                new TypeReference<>() {
                }
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to deserialize outbox event headers",
                    e
            );
        }
    }
}
