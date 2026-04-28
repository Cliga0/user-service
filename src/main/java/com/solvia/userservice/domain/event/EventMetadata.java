package com.solvia.userservice.domain.event;

import com.solvia.userservice.domain.model.vo.metadata.ActorId;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * EventMetadata
 *
 * <p>
 * Contexte immuable décrivant un événement métier.
 *
 * <p>
 * Garantit :
 * - traçabilité distribuée
 * - corrélation des événements
 * - audit complet
 *
 * <p>
 * FAANG-grade, thread-safe, immutable.
 */
public final class EventMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final EventId eventId;
    private final ActorId actorId;
    private final Instant occurredAt;

    private final UUID correlationId;
    private final UUID causationId;

    private final EventSource source;

    private EventMetadata(
        EventId eventId,
        ActorId actorId,
        Instant occurredAt,
        UUID correlationId,
        UUID causationId,
        EventSource source
    ) {
        this.eventId = Objects.requireNonNull(eventId);
        this.actorId = Objects.requireNonNull(actorId);
        this.occurredAt = Objects.requireNonNull(occurredAt);
        this.correlationId = Objects.requireNonNull(correlationId);
        this.causationId = causationId;
        this.source = Objects.requireNonNull(source);
    }

    // ========================
    // INTERNAL FACTORIES
    // ========================

    static EventMetadata root(
        ActorId actorId,
        UUID correlationId,
        EventSource source
    ) {
        return new EventMetadata(
            EventId.generate(),
            actorId,
            Instant.now(),
            correlationId,
            null,
            source
        );
    }

    static EventMetadata child(
        EventMetadata parent,
        ActorId actorId,
        EventSource source
    ) {
        return new EventMetadata(
            EventId.generate(),
            actorId,
            Instant.now(),
            parent.correlationId,
            parent.eventId.value(),
            source
        );
    }

    // ========================
    // 📦 GETTERS
    // ========================

    public EventId eventId() {
        return eventId;
    }

    public ActorId actorId() {
        return actorId;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public UUID correlationId() {
        return correlationId;
    }

    public UUID causationId() {
        return causationId;
    }

    public EventSource source() {
        return source;
    }

    // ========================
    // ⚙️ EQUALITY
    // ========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventMetadata that)) return false;
        return eventId.equals(that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "EventMetadata{" +
            "eventId=" + eventId +
            ", actorId=" + actorId +
            ", occurredAt=" + occurredAt +
            ", correlationId=" + correlationId +
            ", causationId=" + causationId +
            ", source=" + source +
            '}';
    }
}
