package com.solvia.userservice.domain.model.vo.business;

import java.io.Serializable;
import java.util.Objects;

/**
 * LoyaltyPointsReason
 * <p>
 * Raison métier d'un ajout de points.
 */
public final class LoyaltyPointsReason implements Serializable {

    private final String value;

    private LoyaltyPointsReason(String value) {
        this.value = value;
    }

    public static LoyaltyPointsReason of(String value) {
        Objects.requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException("Reason cannot be blank");
        }

        return new LoyaltyPointsReason(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
