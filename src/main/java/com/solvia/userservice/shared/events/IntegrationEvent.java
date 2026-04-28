package com.solvia.userservice.shared.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * IntegrationEvent
 *
 * <p>
 * Contrat d’événement inter-microservices.
 * Stable, sérialisable, indépendant du domain.
 * Immutable et thread-safe.
 */
public final class IntegrationEvent {

    private final UUID eventId;
    private final String eventType;
    private final String aggregateId;
    private final String tenantId;
    private final String traceId;
    private final String payload;
    private final Instant occurredAt;
    private final Map<String, String> headers;

    public IntegrationEvent(
        UUID eventId,
        String eventType,
        String aggregateId,
        String tenantId,
        String traceId,
        String payload,
        Instant occurredAt,
        Map<String, String> headers
    ) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.tenantId = tenantId;
        this.traceId = traceId;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.headers = headers == null ? Map.of() : Map.copyOf(headers);
    }

    public UUID eventId() {
        return eventId;
    }

    public String eventType() {
        return eventType;
    }

    public String aggregateId() {
        return aggregateId;
    }

    public String tenantId() {
        return tenantId;
    }

    public String traceId() {
        return traceId;
    }

    public String payload() {
        return payload;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public Map<String, String> headers() { return headers; }
}
