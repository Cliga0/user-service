package com.solvia.userservice.domain.model.vo.metadata;

import java.io.Serializable;
import java.util.Objects;

/**
 * SuspensionReason
 * <p>
 * Représente la raison métier d'une suspension utilisateur.
 */
public final class SuspensionReason implements Serializable {

    private final String value;

    private SuspensionReason(String value) {
        this.value = value;
    }

    public static SuspensionReason of(String value) {
        Objects.requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException("Suspension reason cannot be blank");
        }

        return new SuspensionReason(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
