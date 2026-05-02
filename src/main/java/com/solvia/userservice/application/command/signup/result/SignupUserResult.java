package com.solvia.userservice.application.command.signup.result;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * SignupUserResult
 * <p>
 * APPLICATION READ MODEL (CQRS OUTPUT)
 * <p>
 * Règles :
 * - Immutable
 * - DTO pur
 * - Aucun concept domain interne exposé
 * - Stable contract API
 */
public record SignupUserResult(
        UUID userId,
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        String birthDate,
        String gender,
        String role,
        boolean activated,
        Instant createdAt
) implements Serializable {}