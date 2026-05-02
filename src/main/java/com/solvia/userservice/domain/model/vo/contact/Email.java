package com.solvia.userservice.domain.model.vo.contact;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object représentant une adresse email.
 *
 * <p>
 * - Immutable
 * - Normalisé (lowercase, trim)
 * - Validé (format raisonnable)
 * </p>
 */
public final class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Regex volontairement simple et robuste (pas RFC complète).
     */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    /**
     * Factory method principale.
     */
    public static Email of(String value) {
        Objects.requireNonNull(value, "Email must not be null");

        String normalized = normalize(value);

        validate(normalized);

        return new Email(normalized);
    }

    public static Email ofNullable(String value) {
        if (value == null || value.isBlank()) return null;
        return of(value);
    }

    /**
     * Normalisation :
     * - trim
     * - lowercase
     */
    private static String normalize(String value) {
        return value.trim().toLowerCase();
    }

    /**
     * Validation métier.
     */
    private static void validate(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        if (value.length() > 254) {
            throw new IllegalArgumentException("Email is too long");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }

    /**
     * Retourne la valeur brute.
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

    /**
     * Comparaison par valeur.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email email)) return false;
        return value.equals(email.value);
    }

    /**
     * Hash basé sur la valeur.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Représentation debug.
     */
    @Override
    public String toString() {
        return value;
    }
}
