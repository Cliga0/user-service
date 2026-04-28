package com.solvia.userservice.domain.model.vo.person;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object représentant un nom de famille.
 *
 * <p>
 * - Immutable
 * - Validé
 * - Normalisé
 * - Support international
 * </p>
 */
public final class LastName implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Support :
     * - lettres unicode
     * - espaces (Van der Meer)
     * - tirets (Smith-Jones)
     * - apostrophes (O'Neil)
     */
    private static final Pattern NAME_PATTERN =
        Pattern.compile("^\\p{L}[\\p{L} '\\-]{0,98}\\p{L}$");

    private final String value;

    private LastName(String value) {
        this.value = value;
    }

    /**
     * Factory method principale.
     */
    public static LastName of(String value) {
        Objects.requireNonNull(value, "LastName must not be null");

        String normalized = normalize(value);

        validate(normalized);

        return new LastName(normalized);
    }

    /**
     * Normalisation :
     * - trim
     * - collapse spaces
     */
    private static String normalize(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    /**
     * Validation métier.
     */
    private static void validate(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("LastName must not be empty");
        }

        if (value.length() < 2) {
            throw new IllegalArgumentException("LastName too short");
        }

        if (value.length() > 100) {
            throw new IllegalArgumentException("LastName too long");
        }

        if (!NAME_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid LastName format: " + value);
        }
    }

    /**
     * Valeur brute.
     */
    public String value() {
        return value;
    }

    /**
     * Alias explicite.
     */
    public String asString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LastName that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
