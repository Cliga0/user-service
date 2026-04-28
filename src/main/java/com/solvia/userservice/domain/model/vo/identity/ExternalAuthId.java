package com.solvia.userservice.domain.model.vo.identity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object représentant l'identifiant d'authentification externe
 * (ex: Keycloak, Auth0, etc.).
 *
 * <p>
 * - Immutable
 * - Non null
 * - Non vide
 * - Indépendant du provider
 * </p>
 */
public final class ExternalAuthId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String value;

    private ExternalAuthId(String value) {
        this.value = value;
    }

    /**
     * Factory method principale.
     */
    public static ExternalAuthId of(String value) {
        Objects.requireNonNull(value, "ExternalAuthId must not be null");

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("ExternalAuthId must not be empty");
        }

        return new ExternalAuthId(normalized);
    }

    /**
     * Retourne la valeur brute (ex: sub JWT).
     */
    public String value() {
        return value;
    }

    /**
     * Représentation String.
     */
    public String asString() {
        return value;
    }

    /**
     * Comparaison basée sur la valeur.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalAuthId that)) return false;
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
     * Debug/logging.
     */
    @Override
    public String toString() {
        return value;
    }
}
