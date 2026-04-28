package com.solvia.userservice.domain.model.vo.metadata;

import com.solvia.userservice.domain.model.vo.identity.UserId;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * ActorId
 * <p>
 * Identité métier de l'acteur ayant déclenché une action :
 * - utilisateur
 * - service
 * - système externe
 * <p>
 * Value Object immuable et thread-safe.
 */
public final class ActorId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID value;

    private ActorId(UUID value) {
        this.value = value;
    }

    /**
     * Crée un ActorId à partir d'un UUID existant.
     */
    public static ActorId of(UUID value) {
        Objects.requireNonNull(value, "ActorId value must not be null");
        return new ActorId(value);
    }

    public static ActorId of(UserId userId) {
        Objects.requireNonNull(userId);
        return new ActorId(userId.value());
    }

    /**
     * Crée un ActorId à partir d'une String UUID.
     *
     * @throws IllegalArgumentException si la chaîne est invalide
     */
    public static ActorId fromString(String value) {
        Objects.requireNonNull(value, "ActorId string must not be null");
        return new ActorId(UUID.fromString(value));
    }

    /**
     * Génère un nouvel ActorId aléatoire.
     * Typiquement utilisé pour des acteurs techniques ou systèmes.
     */
    public static ActorId generate() {
        return new ActorId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActorId actorId)) return false;
        return value.equals(actorId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public String asString() { return value.toString(); }
}
