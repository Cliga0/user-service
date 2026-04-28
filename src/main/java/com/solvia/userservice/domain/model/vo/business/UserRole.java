package com.solvia.userservice.domain.model.vo.business;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * UserRole
 * <p>
 * DOMAIN VALUE OBJECT (PURE)
 * <p>
 * Responsabilités :
 * ✔ Représenter un rôle métier
 * ✔ Garantir l'intégrité du concept
 * <p>
 * ❌ Aucun parsing IAM
 * ❌ Aucun détail d'infrastructure
 * <p>
 * Architecture rule:
 * IAM → ACL → Domain (never direct)
 */
public final class UserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Code code;

    private UserRole(Code code) {
        this.code = Objects.requireNonNull(code, "UserRole code must not be null");
    }

    /**
     * Factory method principale.
     */
    public static UserRole of(Code code) {
        return new UserRole(code);
    }

    /**
     * Domain behavior.
     */
    public boolean isAdmin() {
        return this.code == Code.ADMIN;
    }

    public boolean isSupport() {
        return this.code == Code.SUPPORT;
    }

    public boolean isCustomer() {
        return this.code == Code.CUSTOMER;
    }

    public boolean isSeller() {
        return this.code == Code.SELLER;
    }

    /**
     * Retourne le code métier.
     */
    public Code code() {
        return code;
    }

    public String value() {
        return code.name();
    }

    public String asString() {
        return code.value();
    }

    /**
     * Equality.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole that)) return false;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return code.name();
    }

    /**
     * Codes métier stables.
     */
    public enum Code {

        CUSTOMER("customer"),
        SELLER("seller"),
        ADMIN("admin"),
        SUPPORT("support");

        private final String value;

        Code(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static Code from(String raw) {
            Objects.requireNonNull(raw, "Role cannot be null");

            String normalized = raw.trim().toLowerCase();

            for (Code c : values()) {
                if (c.value.equals(normalized)) {
                    return c;
                }
            }

            throw new IllegalArgumentException("Unknown role: " + raw);
        }
    }
}
