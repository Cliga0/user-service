package com.solvia.userservice.domain.event;

import java.time.Instant;

/**
 * DomainEvent
 *
 * <p>
 * Contrat racine de tous les événements métier.
 *
 * <p>
 * Un DomainEvent représente un fait métier IMMUTABLE
 * survenu dans le domaine.
 *
 * <p>
 * - Stable (contract-safe)
 * - Refactor-safe
 * - Event-driven ready
 * - Distribué-friendly
 */
public interface DomainEvent {

    /**
     * Métadonnées de l'événement.
     * <p>
     * Contient :
     * - eventId
     * - actorId
     * - occurredAt
     * - correlationId
     * - causationId
     * - source
     * - version
     */
    EventMetadata metadata();

    /**
     * Instant précis du fait métier.
     */
    default Instant occurredAt() {
        return metadata().occurredAt();
    }

    /**
     * Type stable et versionné de l'événement.
     */
    EventType eventType();
}
