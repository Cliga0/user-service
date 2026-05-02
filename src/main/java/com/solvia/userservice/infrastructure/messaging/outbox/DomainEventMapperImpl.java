package com.solvia.userservice.infrastructure.messaging.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvia.userservice.domain.event.DomainEvent;
import com.solvia.userservice.domain.event.EventMetadata;
import com.solvia.userservice.domain.event.user.UserCreatedEvent;
import com.solvia.userservice.shared.events.IntegrationEvent;
import com.solvia.userservice.shared.events.user.UserCreatedPayload;

import java.util.Map;
import java.util.Objects;

/**
 * DomainEventMapperImpl
 *
 * <p>
 * Anti-Corruption Layer (ACL) entre :
 * Domain Events → Integration Events (Outbox / Kafka)
 *
 * <p>
 * Responsabilités :
 * - Transformer les Domain Events en contrats d’intégration stables
 * - Isoler complètement le domaine de la sérialisation JSON
 * - Garantir compatibilité versionnée des événements
 *
 * <p>
 * FAANG-grade :
 * - extensible (pattern mapping explicite)
 * - safe (pas de réflexion implicite Jackson sur le domain)
 * - testable
 */
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
                resolveTenant(meta),
                meta.correlationId().toString(),
                serializePayload(event),
                event.occurredAt(),
                Map.of()
        );
    }

    // =====================================================
    // SERIALIZATION LAYER (ONLY PAYLOAD)
    // =====================================================

    private String serializePayload(DomainEvent event) {
        try {
            Object payload = toPayload(event);
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize integration payload", e);
        }
    }

    // =====================================================
    // DOMAIN → INTEGRATION MAPPING
    // =====================================================

    private Object toPayload(DomainEvent event) {

        if (event instanceof UserCreatedEvent e) {

            return new UserCreatedPayload(
                    e.metadata().eventId().value().toString(),
                    e.userId().value().toString(),
                    e.email().value(),
                    e.status().name(),
                    e.metadata().occurredAt(),
                    e.metadata().correlationId()
            );
        }

        throw new IllegalStateException(
                "Unsupported domain event for mapping: " + event.getClass().getSimpleName()
        );
    }

    // =====================================================
    // METADATA HELPERS
    // =====================================================

    private String extractAggregateId(EventMetadata meta) {
        return meta.actorId().value().toString();
    }

    private String resolveTenant(EventMetadata meta) {
        return "default";
    }
}