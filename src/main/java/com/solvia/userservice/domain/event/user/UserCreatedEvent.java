package com.solvia.userservice.domain.event.user;

import com.solvia.userservice.domain.event.DomainEvent;
import com.solvia.userservice.domain.event.EventId;
import com.solvia.userservice.domain.event.EventMetadata;
import com.solvia.userservice.domain.event.EventType;
import com.solvia.userservice.domain.model.vo.contact.Email;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.domain.model.vo.metadata.UserMetadata.UserStatus;

import java.util.Objects;

/**
 * UserCreatedEvent
 * <p>
 * Domain Event pur :
 * - Représente un fait métier
 * - Immutable
 * - Indépendant de toute infrastructure (JSON, DB, Kafka)
 * - Stable contractuellement
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

    // ─────────────────────────────────────────────
    // Factory (Domain-friendly)
    // ─────────────────────────────────────────────

    public static UserCreatedEvent of(
            EventMetadata metadata,
            UserId userId,
            Email email,
            UserStatus status
    ) {
        return new UserCreatedEvent(metadata, userId, email, status);
    }

    // ─────────────────────────────────────────────
    // DomainEvent contract
    // ─────────────────────────────────────────────

    public EventId eventId() {
        return metadata.eventId();
    }

    @Override
    public EventType eventType() {
        return TYPE;
    }

    @Override
    public EventMetadata metadata() {
        return metadata;
    }

    // ─────────────────────────────────────────────
    // Business data (rich domain model)
    // ─────────────────────────────────────────────

    public UserId userId() {
        return userId;
    }

    public Email email() {
        return email;
    }

    public UserStatus status() {
        return status;
    }

    // ─────────────────────────────────────────────
    // Identity semantics (critical for outbox/idempotency)
    // ─────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCreatedEvent that)) return false;
        return metadata.eventId().equals(that.metadata.eventId());
    }

    @Override
    public int hashCode() {
        return metadata.eventId().hashCode();
    }

    @Override
    public String toString() {
        return "UserCreatedEvent{" +
                "userId=" + userId +
                ", email=" + email +
                ", status=" + status +
                ", metadata=" + metadata +
                '}';
    }
}