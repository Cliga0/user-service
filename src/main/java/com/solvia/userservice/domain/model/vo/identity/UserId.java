package com.solvia.userservice.domain.model.vo.identity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object représentant l'identifiant unique d'un utilisateur.
 *
 * <p>
 * - Immutable
 * - Thread-safe
 * - Validé à la création
 * </p>
 */
public final class UserId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID value;

    private UserId(UUID value) {
        this.value = Objects.requireNonNull(value, "UserId value must not be null");
    }

    /**
     * Crée un UserId à partir d'un UUID existant.
     */
    public static UserId of(UUID value) {
        return new UserId(value);
    }

    /**
     * Crée un UserId à partir d'une String (ex: venant de Keycloak "sub").
     */
    public static UserId fromString(String value) {
        Objects.requireNonNull(value, "UserId string must not be null");

        try {
            return new UserId(UUID.fromString(value));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid UserId format: " + value, ex);
        }
    }

    public static UserId ofNullable(String value) {
        if (value == null || value.isBlank()) return null;
        return fromString(value);
    }

    /**
     * Génère un nouvel UserId.
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    /**
     * Retourne la valeur UUID brute.
     */
    public UUID value() {
        return value;
    }

    /**
     * Retourne la représentation String (utile pour API, logs, etc.).
     */
    public String asString() {
        return value.toString();
    }

    /**
     * Comparaison basée sur la valeur (Value Object).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId that)) return false;
        return value.equals(that.value);
    }

    /**
     * Hash basé sur la valeur.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Représentation lisible (debug).
     */
    @Override
    public String toString() {
        return value.toString();
    }
}
