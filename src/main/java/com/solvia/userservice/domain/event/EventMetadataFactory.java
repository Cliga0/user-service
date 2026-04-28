package com.solvia.userservice.domain.event;

import com.solvia.userservice.domain.model.vo.metadata.ActorId;
import com.solvia.userservice.domain.model.vo.metadata.SystemActors;

import java.util.Objects;
import java.util.UUID;

/**
 * EventMetadataFactory
 * <p>
 * API UNIQUE pour créer des EventMetadata.
 */
public final class EventMetadataFactory {

    private static final EventSource DEFAULT_SOURCE =
        EventSource.of("user-service", "v1");

    private EventMetadataFactory() {}

    // ========================
    // ROOT EVENT
    // ========================

    public static EventMetadata now(ActorId actorId) {
        return now(actorId, DEFAULT_SOURCE);
    }

    public static EventMetadata now(
        ActorId actorId,
        EventSource source
    ) {
        Objects.requireNonNull(actorId);

        return EventMetadata.root(
            actorId,
            UUID.randomUUID(),
            source
        );
    }

    // ========================
    // SYSTEM EVENT
    // ========================

    public static EventMetadata system() {
        return now(SystemActors.SYSTEM);
    }

    // ========================
    // CORRELATED EVENT
    // ========================

    public static EventMetadata correlated(
        EventMetadata parent,
        ActorId actorId
    ) {
        return correlated(parent, actorId, parent.source());
    }

    public static EventMetadata correlated(
        EventMetadata parent,
        ActorId actorId,
        EventSource source
    ) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(actorId);

        return EventMetadata.child(parent, actorId, source);
    }
}
