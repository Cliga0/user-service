package com.solvia.userservice.domain.model.vo.person;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object représentant un prénom.
 *
 * <p>
 * - Immutable
 * - Validé
 * - Normalisé
 * </p>
 */
public final class FirstName implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Autorise :
     * - lettres (unicode)
     * - espaces
     * - tirets
     * - apostrophes
     */
    private static final Pattern NAME_PATTERN =
        Pattern.compile("^\\p{L}[\\p{L} '\\-]{0,48}\\p{L}$");

    private final String value;

    private FirstName(String value) {
        this.value = value;
    }

    /**
     * Factory method principale.
     */
    public static FirstName of(String value) {
        Objects.requireNonNull(value, "FirstName must not be null");

        String normalized = normalize(value);

        validate(normalized);

        return new FirstName(normalized);
    }

    /**
     * Normalisation :
     * - trim
     * - collapse spaces (optionnel léger)
     */
    private static String normalize(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    /**
     * Validation métier.
     */
    private static void validate(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("FirstName must not be empty");
        }

        if (value.length() < 2) {
            throw new IllegalArgumentException("FirstName too short");
        }

        if (value.length() > 50) {
            throw new IllegalArgumentException("FirstName too long");
        }

        if (!NAME_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid FirstName format: " + value);
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
        if (!(o instanceof FirstName that)) return false;
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
