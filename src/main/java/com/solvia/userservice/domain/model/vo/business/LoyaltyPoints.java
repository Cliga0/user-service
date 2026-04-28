package com.solvia.userservice.domain.model.vo.business;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object représentant les points de fidélité.
 *
 * <p>
 * - Immutable
 * - Non négatif
 * - Opérations contrôlées
 * </p>
 */
public final class LoyaltyPoints implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int value;

    private LoyaltyPoints(int value) {
        this.value = value;
    }

    /**
     * Factory method principale.
     */
    public static LoyaltyPoints of(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("LoyaltyPoints cannot be negative");
        }
        return new LoyaltyPoints(value);
    }

    /**
     * Points initiaux (0).
     */
    public static LoyaltyPoints zero() {
        return new LoyaltyPoints(0);
    }

    /**
     * Ajout de points (immutable → nouveau objet).
     */
    public LoyaltyPoints add(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Cannot add negative points");
        }
        return new LoyaltyPoints(this.value + points);
    }

    /**
     * Déduction de points (avec protection).
     */
    public LoyaltyPoints subtract(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Cannot subtract negative points");
        }

        int result = this.value - points;

        if (result < 0) {
            throw new IllegalStateException("Not enough loyalty points");
        }

        return new LoyaltyPoints(result);
    }

    /**
     * Retourne la valeur brute.
     */
    public int value() {
        return value;
    }

    /**
     * Vérifie si suffisant.
     */
    public boolean isGreaterOrEqual(int points) {
        return this.value >= points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoyaltyPoints that)) return false;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
