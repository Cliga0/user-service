package com.solvia.userservice.interfaces.web.rest.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * CreateUserResponse
 * <p>
 * API CONTRACT (OUTPUT)
 * <p>
 * RESPONSABILITÉS :
 * - représenter la réponse HTTP
 * - exposer un état minimal et stable
 * - supporter la navigation API
 * <p>
 * ❌ Aucun comportement métier
 * ❌ Aucun couplage au domain
 * <p>
 * ✔ Immutable
 * ✔ Versionnable
 * ✔ Consumer-friendly
 */
public record CreateUserResponse(

        // =========================
        // RESOURCE IDENTITY
        // =========================

        UUID userId,

        // =========================
        // STATE SNAPSHOT
        // =========================

        String email,
        String role,

        // =========================
        // OPERATION METADATA
        // =========================

        boolean created,
        Instant createdAt,

        // =========================
        // HATEOAS / NAVIGATION
        // =========================

        String self

) {}