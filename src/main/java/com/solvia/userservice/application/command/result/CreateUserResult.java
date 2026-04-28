package com.solvia.userservice.application.command.result;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * CreateUserResult
 * <p>
 * APPLICATION DTO
 * <p>
 * Représente la réponse du use case CreateUser
 * <p>
 * ✔ Immutable
 * ✔ Stable contract
 * ✔ Sans dépendance domaine
 * ✔ Transport agnostic (REST / Kafka / gRPC)
 * <p>
 * ⚠️ Aucun comportement métier
 */
public record CreateUserResult(

        // =========================
        // RESOURCE IDENTITY
        // =========================
        UUID userId,

        // =========================
        // STATE SNAPSHOT (MINIMAL)
        // =========================
        String email,
        String role,

        // =========================
        // OPERATION METADATA
        // =========================
        boolean created,
        Instant createdAt,

        // =========================
        // API / NAVIGATION SUPPORT
        // =========================
        String self

) implements Serializable {}