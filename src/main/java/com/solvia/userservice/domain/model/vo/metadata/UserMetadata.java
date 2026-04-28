package com.solvia.userservice.domain.model.vo.metadata;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * UserMetadata
 *
 * <p>
 * Représente les métadonnées techniques et d'audit
 * d'un utilisateur.
 *
 * <p>
 * Responsabilités :
 * - Traçabilité (audit léger)
 * - Lifecycle state
 * - Historique de modification
 *
 * <p>
 * Immutable, thread-safe, DDD-compliant.
 */
public final class UserMetadata implements Serializable {

    private final Instant createdAt;
    private final Instant updatedAt;

    private final ActorId createdBy;
    private final ActorId updatedBy;

    private final UserStatus status;

    private UserMetadata(
        Instant createdAt,
        Instant updatedAt,
        ActorId createdBy,
        ActorId updatedBy,
        UserStatus status
    ) {
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.createdBy = Objects.requireNonNull(createdBy);
        this.updatedBy = Objects.requireNonNull(updatedBy);
        this.status = Objects.requireNonNull(status);
    }

    // =========================
    // FACTORY
    // =========================

    public static UserMetadata create(ActorId actor) {
        Instant now = Instant.now();

        return new UserMetadata(
            now,
            now,
            actor,
            actor,
            UserStatus.ACTIVE
        );
    }

    public static UserMetadata rehydrate(
        Instant createdAt,
        Instant updatedAt,
        ActorId createdBy,
        ActorId updatedBy,
        UserStatus status
    ) {
        return new UserMetadata(
            createdAt,
            updatedAt,
            createdBy,
            updatedBy,
            status
        );
    }

    // =========================
    // TRANSITIONS SAFE
    // =========================

    public UserMetadata update(ActorId actor) {
        ensureNotDeleted();

        return new UserMetadata(
            this.createdAt,
            Instant.now(),
            this.createdBy,
            actor,
            this.status
        );
    }

    public UserMetadata changeStatus(UserStatus newStatus, ActorId actor) {
        ensureNotDeleted();
        ensureValidTransition(newStatus);

        return new UserMetadata(
            this.createdAt,
            Instant.now(),
            this.createdBy,
            actor,
            newStatus
        );
    }

    // =========================
    // BUSINESS RULES
    // =========================

    private void ensureNotDeleted() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("User is deleted and cannot be modified");
        }
    }

    private void ensureValidTransition(UserStatus newStatus) {
        if (this.status == UserStatus.SUSPENDED && newStatus == UserStatus.ACTIVE) {
            return;
        }

        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("Cannot transition from DELETED");
        }
    }

    // =========================
    // GETTERS
    // =========================

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public ActorId createdBy() {
        return createdBy;
    }

    public ActorId updatedBy() {
        return updatedBy;
    }

    public UserStatus status() {
        return status;
    }

    // =========================
    // STATUS
    // =========================

    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        DELETED
    }
}
