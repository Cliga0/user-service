package com.solvia.userservice.domain.event;

import java.io.Serializable;
import java.util.Objects;

/**
 * EventType
 * <p>
 * Représente le contrat logique d'un événement.
 * <p>
 * Format recommandé :
 * domain.action.version
 * <p>
 * Exemple :
 * user.created.v1
 * user.email.changed.v1
 */
public final class EventType implements Serializable {

    private final String value;

    private EventType(String value) {
        this.value = value;
    }

    public static EventType of(String value) {
        validate(value);
        return new EventType(value);
    }

    private static void validate(String value) {
        Objects.requireNonNull(value);

        if (!value.matches("^[a-z]+(\\.[a-z]+)*\\.v[0-9]+$")) {
            throw new IllegalArgumentException(
                "Invalid EventType format: " + value
            );
        }
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
