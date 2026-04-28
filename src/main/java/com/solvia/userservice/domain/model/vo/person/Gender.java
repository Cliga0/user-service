package com.solvia.userservice.domain.model.vo.person;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object représentant le genre.
 *
 * <p>
 * - Immutable
 * - Basé sur un code stable
 * - Extensible sans casser le domain
 * </p>
 */
public final class Gender implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Code code;

    private Gender(Code code) {
        this.code = code;
    }

    public static Gender of(Code code) {
        Objects.requireNonNull(code, "Gender code must not be null");
        return new Gender(code);
    }

    public static Gender fromString(String value) {
        Objects.requireNonNull(value, "Gender must not be null");

        return new Gender(Code.from(value));
    }

    public Code code() {
        return code;
    }

    public String asString() {
        return code.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gender gender)) return false;
        return code == gender.code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return code.value;
    }

    /**
     * Codes métier stables.
     */
    public enum Code {
        MALE("MALE"),
        FEMALE("FEMALE"),
        OTHER("OTHER"),
        UNKNOWN("UNKNOWN");

        private final String value;

        Code(String value) {
            this.value = value;
        }

        public static Code from(String raw) {
            if (raw == null || raw.trim().isEmpty()) {
                throw new IllegalArgumentException("Gender cannot be empty");
            }

            for (Code c : values()) {
                if (c.value.equalsIgnoreCase(raw.trim())) {
                    return c;
                }
            }

            return UNKNOWN;
        }
    }
}
