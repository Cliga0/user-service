package com.solvia.userservice.domain.event;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * EventId
 * <p>
 * Identifiant unique d'un événement métier.
 * <p>
 * Rôles principaux :
 * - Idempotence des consommateurs
 * - Traçabilité distribuée
 * - Audit et debugging
 * <p>
 * Value Object immuable, thread-safe et refactor-safe.
 */
public final class EventId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID value;

    private EventId(UUID value) {
        this.value = value;
    }

    /**
     * Crée un EventId à partir d'un UUID existant.
     */
    public static EventId of(UUID value) {
        Objects.requireNonNull(value, "EventId value must not be null");
        return new EventId(value);
    }

    /**
     * Crée un EventId à partir d'une représentation String UUID.
     *
     * @throws IllegalArgumentException si la chaîne n'est pas un UUID valide
     */
    public static EventId fromString(String value) {
        Objects.requireNonNull(value, "EventId string must not be null");
        return new EventId(UUID.fromString(value));
    }

    /**
     * Génère un nouvel EventId unique.
     * <p>
     * Utilisé lors de la création d'un nouvel événement métier.
     */
    public static EventId generate() {
        return new EventId(UUID.randomUUID());
    }

    /**
     * Retourne la valeur brute UUID.
     * Usage réservé aux couches techniques (persistance, messaging).
     */
    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventId eventId)) return false;
        return value.equals(eventId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
