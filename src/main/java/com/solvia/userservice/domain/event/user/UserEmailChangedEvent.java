package com.solvia.userservice.domain.event.user;

import com.solvia.userservice.domain.event.DomainEvent;
import com.solvia.userservice.domain.event.EventMetadata;
import com.solvia.userservice.domain.event.EventType;
import com.solvia.userservice.domain.model.vo.contact.Email;
import com.solvia.userservice.domain.model.vo.identity.UserId;

import java.util.Objects;

/**
 * UserEmailChangedEvent
 *
 * <p>
 * Événement métier déclenché lorsqu'un utilisateur modifie son email.
 *
 * <p>
 * Contient l'ancien et le nouvel email pour audit et traçabilité.
 *
 * <p>
 * Immutable, stable, contract-safe.
 */
public final class UserEmailChangedEvent implements DomainEvent {

    private static final EventType TYPE = EventType.of("user.email.changed.v1");

    private final EventMetadata metadata;

    private final UserId userId;
    private final Email oldEmail;
    private final Email newEmail;

    private UserEmailChangedEvent(
        EventMetadata metadata,
        UserId userId,
        Email oldEmail,
        Email newEmail
    ) {
        this.metadata = Objects.requireNonNull(metadata);
        this.userId = Objects.requireNonNull(userId);
        this.oldEmail = Objects.requireNonNull(oldEmail);
        this.newEmail = Objects.requireNonNull(newEmail);

        validate();
    }

    // =========================
    // FACTORY
    // =========================

    public static UserEmailChangedEvent of(
        EventMetadata metadata,
        UserId userId,
        Email oldEmail,
        Email newEmail
    ) {
        return new UserEmailChangedEvent(
            metadata,
            userId,
            oldEmail,
            newEmail
        );
    }

    // =========================
    // VALIDATION
    // =========================

    private void validate() {
        if (oldEmail.equals(newEmail)) {
            throw new IllegalArgumentException("New email must be different from old email");
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

    public Email oldEmail() {
        return oldEmail;
    }

    public Email newEmail() {
        return newEmail;
    }
}
