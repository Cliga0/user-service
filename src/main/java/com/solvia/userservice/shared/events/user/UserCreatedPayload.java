package com.solvia.userservice.shared.events.user;

import java.time.Instant;
import java.util.UUID;

/**
 * UserCreatedPayload
 *
 * <p>
 * Contrat d'intégration stable (OUTBOX / KAFKA / API EVENTS).
 *
 * <p>
 * IMPORTANT :
 * - Aucun type métier (UserId, Email VO)
 * - Only primitives / Strings
 * - Versionable
 * - Serializable-safe
 */
public record UserCreatedPayload(

        String eventId,
        String userId,
        String email,
        String status,
        Instant occurredAt,
        UUID correlationId

) {}