package com.solvia.userservice.application.query.result;

import java.io.Serializable;
import java.util.Objects;

/**
 * READ MODEL (CQRS Query Result)
 * <p>
 * ⚠️ Ce n'est PAS du domaine
 * ✔ Contrat de lecture stable pour API / UI / clients externes
 * ✔ Optimisé pour sérialisation
 * ✔ Indépendant des Value Objects
 */
public record GetUserResult(

        // =========================
        // IDENTITY
        // =========================
        String userId,

        // =========================
        // PROFILE
        // =========================
        String firstName,
        String lastName,
        String birthDate,
        String gender,

        // =========================
        // CONTACT
        // =========================
        String email,
        String phoneNumber,

        // =========================
        // BUSINESS
        // =========================
        String role,

        // =========================
        // AUDIT / STATE (READ SIDE ONLY)
        // =========================
        String status

) implements Serializable {

    public GetUserResult {
        Objects.requireNonNull(userId, "userId is required");
        Objects.requireNonNull(firstName, "firstName is required");
        Objects.requireNonNull(lastName, "lastName is required");
    }

    // =========================
    // FACTORY (OPTIONAL CLEAN ENTRY)
    // =========================

    public static GetUserResult of(
            String userId,
            String firstName,
            String lastName,
            String birthDate,
            String gender,
            String email,
            String phoneNumber,
            String role,
            String status
    ) {
        return new GetUserResult(
                userId,
                firstName,
                lastName,
                birthDate,
                gender,
                email,
                phoneNumber,
                role,
                status
        );
    }
}