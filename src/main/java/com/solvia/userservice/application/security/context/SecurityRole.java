package com.solvia.userservice.application.security.context;

import java.util.Objects;

/**
 * Représente un rôle provenant du provider IAM (Keycloak, Auth0, etc.)
 * Aucun lien avec le domaine.
 */
public record SecurityRole(String value) {

    public SecurityRole {
        Objects.requireNonNull(value, "SecurityRole value must not be null");
        value = normalize(value);
    }

    private static String normalize(String value) {
        String v = value.trim().toUpperCase();
        if (v.isEmpty()) {
            throw new IllegalArgumentException("SecurityRole cannot be empty");
        }
        return v;
    }

    public static SecurityRole of(String value) {
        return new SecurityRole(value);
    }
}