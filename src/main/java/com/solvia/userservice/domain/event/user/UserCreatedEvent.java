package com.solvia.userservice.domain.event.user;

import com.solvia.userservice.domain.event.DomainEvent;
import com.solvia.userservice.domain.event.EventMetadata;
import com.solvia.userservice.domain.event.EventType;
import com.solvia.userservice.domain.model.vo.contact.Email;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.domain.model.vo.metadata.UserMetadata.UserStatus;

import java.util.Objects;

/**
 * UserCreatedEvent
 *
 * <p>
 * Événement métier déclenché lors de la création d'un utilisateur.
 *
 * <p>
 * Immutable, stable, contract-safe.
 */
public final class UserCreatedEvent implements DomainEvent {

    private static final EventType TYPE = EventType.of("user.created.v1");

    private final EventMetadata metadata;

    private final UserId userId;
    private final Email email;
    private final UserStatus status;

    private UserCreatedEvent(
        EventMetadata metadata,
        UserId userId,
        Email email,
        UserStatus status
    ) {
        this.metadata = Objects.requireNonNull(metadata);
        this.userId = Objects.requireNonNull(userId);
        this.email = Objects.requireNonNull(email);
        this.status = Objects.requireNonNull(status);
    }

    // =========================
    // FACTORY
    // =========================

    public static UserCreatedEvent of(
        EventMetadata metadata,
        UserId userId,
        Email email,
        UserStatus status
    ) {
        return new UserCreatedEvent(metadata, userId, email, status);
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

    public Email email() {
        return email;
    }

    public UserStatus status() {
        return status;
    }
}
