package com.solvia.userservice.domain.model.vo.metadata;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * SystemActors
 *
 * <p>
 * Fournit des identités immuables et centralisées pour tous les acteurs système
 * prédéfinis dans l'application.
 * Utile pour les événements métier, logs, actions automatiques ou intégrations internes.
 *
 * <p>
 * Enterprise-grade, thread-safe, ready for large-scale systems.
 */
public final class SystemActors {

    private SystemActors() {
        // Prevent instantiation
    }

    // ──────────────── Acteurs prédéfinis ──────────────────────
    public static final ActorId SYSTEM = ActorId.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    public static final ActorId SCHEDULER = ActorId.of(UUID.fromString("00000000-0000-0000-0000-000000000002"));
    public static final ActorId INTEGRATION_SERVICE = ActorId.of(UUID.fromString("00000000-0000-0000-0000-000000000003"));

    // Mapping interne pour lookup rapide par nom
    private static final Map<String, ActorId> ACTOR_MAP = Map.of(
            "SYSTEM", SYSTEM,
            "SCHEDULER", SCHEDULER,
            "INTEGRATION_SERVICE", INTEGRATION_SERVICE
    );

    // ──────────────── Méthodes publiques ──────────────────────

    /**
     * Retourne l'acteur système prédéfini par nom.
     *
     * @param name Nom de l'acteur (SYSTEM, SCHEDULER, INTEGRATION_SERVICE)
     * @return ActorId correspondant
     * @throws IllegalArgumentException si le nom est inconnu
     */
    public static ActorId of(String name) {
        Objects.requireNonNull(name, "Actor name must not be null");
        ActorId actor = ACTOR_MAP.get(name.toUpperCase());
        if (actor == null) {
            throw new IllegalArgumentException("Unknown system actor: " + name);
        }
        return actor;
    }

    /**
     * Génère un nouvel ActorId unique pour des besoins exceptionnels.
     * <p>
     * Pour la plupart des usages, privilégiez les acteurs prédéfinis.
     *
     * @return nouvel ActorId aléatoire
     */
    public static ActorId generate() {
        return ActorId.generate();
    }
}
