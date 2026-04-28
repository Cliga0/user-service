package com.solvia.userservice.domain.event.user;

import com.solvia.userservice.domain.event.DomainEvent;
import com.solvia.userservice.domain.event.EventMetadata;
import com.solvia.userservice.domain.event.EventType;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.domain.model.vo.metadata.SuspensionReason;

import java.time.Instant;
import java.util.Objects;

/**
 * UserSuspendedEvent
 *
 * <p>
 * Événement métier déclenché lorsqu'un utilisateur est suspendu.
 *
 * <p>
 * Contient la raison et éventuellement une date de fin.
 *
 * <p>
 * Critique pour sécurité, audit et conformité.
 */
public final class UserSuspendedEvent implements DomainEvent {

    private static final EventType TYPE = EventType.of("user.suspended.v1");

    private final EventMetadata metadata;

    private final UserId userId;
    private final SuspensionReason reason;
    private final Instant suspendedUntil; // nullable

    private UserSuspendedEvent(
        EventMetadata metadata,
        UserId userId,
        SuspensionReason reason,
        Instant suspendedUntil
    ) {
        this.metadata = Objects.requireNonNull(metadata);
        this.userId = Objects.requireNonNull(userId);
        this.reason = Objects.requireNonNull(reason);
        this.suspendedUntil = suspendedUntil;

        validate();
    }

    // =========================
    // FACTORY
    // =========================

    public static UserSuspendedEvent of(
        EventMetadata metadata,
        UserId userId,
        SuspensionReason reason,
        Instant suspendedUntil
    ) {
        return new UserSuspendedEvent(
            metadata,
            userId,
            reason,
            suspendedUntil
        );
    }

    // =========================
    // VALIDATION
    // =========================

    private void validate() {
        if (suspendedUntil != null && suspendedUntil.isBefore(metadata.occurredAt())) {
            throw new IllegalArgumentException("Suspension end must be in the future");
        }
    }

    // =========================
    // DOMAIN EVENT
    // =========================

    @Override
    public EventMetadata metadata() {
        return metadata;
    }

    @Override
    public EventType eventType() {
        return TYPE;
    }

    // =========================
    // GETTERS
    // =========================

    public UserId userId() {
        return userId;
    }

    public SuspensionReason reason() {
        return reason;
    }

    public Instant suspendedUntil() {
        return suspendedUntil;
    }
}
