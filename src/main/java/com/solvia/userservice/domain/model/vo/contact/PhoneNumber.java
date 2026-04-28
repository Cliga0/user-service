package com.solvia.userservice.domain.model.vo.contact;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object représentant un numéro de téléphone.
 *
 * <p>
 * - Immutable
 * - Normalisé au format E.164
 * - Validation stricte mais réaliste
 * </p>
 */
public final class PhoneNumber implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Format E.164 : + suivi de 8 à 15 chiffres
     */
    private static final Pattern E164_PATTERN =
        Pattern.compile("^\\+[1-9]\\d{7,14}$");

    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    /**
     * Factory method principale.
     */
    public static PhoneNumber of(String raw) {
        Objects.requireNonNull(raw, "Phone number must not be null");

        String normalized = normalize(raw);

        validate(normalized);

        return new PhoneNumber(normalized);
    }

    /**
     * Normalisation :
     * - supprime espaces, tirets, parenthèses
     * - conserve uniquement + et chiffres
     */
    private static String normalize(String raw) {
        String cleaned = raw.trim()
            .replaceAll("[\\s\\-()]", "");

        // cas : commence par 00 → remplacer par +
        if (cleaned.startsWith("00")) {
            cleaned = "+" + cleaned.substring(2);
        }

        return cleaned;
    }

    /**
     * Validation format E.164.
     */
    private static void validate(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Phone number must not be empty");
        }

        if (!E164_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid phone number format: " + value);
        }
    }

    /**
     * Retourne la valeur normalisée (E.164).
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
        if (!(o instanceof PhoneNumber that)) return false;
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
