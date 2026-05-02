package com.solvia.userservice.interfaces.web.rest.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * SignupUserResponse
 * <p>
 * API OUTPUT MODEL (EDGE LAYER)
 * <p>
 * RESPONSIBILITY:
 * - expose safe, stable API representation of signup result
 * <p>
 * STRICT RULES:
 * - no domain leakage
 * - no internal IDs structure exposure
 * - no business logic
 */
public record SignupUserResponse(

        UUID userId,

        String email,

        String firstName,

        String lastName,

        String role,

        Instant createdAt,

        boolean activated

) {}