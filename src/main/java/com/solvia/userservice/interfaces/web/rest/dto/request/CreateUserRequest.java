package com.solvia.userservice.interfaces.web.rest.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * CreateUserRequest
 * <p>
 * API CONTRACT (INPUT)
 * <p>
 * RESPONSABILITÉS :
 * - représenter la requête HTTP
 * - valider les contraintes de format
 * - rester stable dans le temps
 * <p>
 * ❌ Aucun comportement métier
 * ❌ Aucun mapping
 * ❌ Aucun couplage au domain
 * <p>
 * ✔ Immutable
 * ✔ Serializable (JSON)
 * ✔ Backward compatible friendly
 */
public record CreateUserRequest(

        // =========================
        // IDENTITY (optional from client)
        // =========================

        String externalAuthId,

        // =========================
        // PERSON
        // =========================

        @NotBlank
        @Size(max = 100)
        String firstName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @NotNull
        LocalDate birthDate,

        @NotBlank
        @Pattern(regexp = "male|female|other", message = "Invalid gender")
        String gender,

        // =========================
        // CONTACT
        // =========================

        @Email
        @Size(max = 255)
        String email,

        @Size(max = 20)
        String phoneNumber,

        // =========================
        // BUSINESS
        // =========================

        @NotBlank
        @Size(max = 50)
        String role

) {}