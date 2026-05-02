package com.solvia.userservice.shared;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * TenantId
 *
 * <p>
 * Identité unique d’un tenant dans un système multi-tenant
 * (client, organisation, compte SaaS).
 *
 * <p>
 * Responsabilités :
 * <ul>
 *     <li>Isolation logique des données</li>
 *     <li>Clé de partition (DB, cache, events)</li>
 *     <li>Contexte de sécurité et de routage</li>
 * </ul>
 *
 * <p>
 * Caractéristiques :
 * <ul>
 *     <li>Value Object DDD</li>
 *     <li>Immutable et thread-safe</li>
 *     <li>Basé sur UUID (safe en distribué)</li>
 *     <li>Comparable et Serializable</li>
 * </ul>
 *
 * <p>
 * Toute entité persistée DOIT être rattachée à un TenantId.
 */
public final class TenantId
        implements Serializable, Comparable<TenantId> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID value;

    // -------------------- CONSTRUCTEUR PRIVÉ --------------------

    private TenantId(UUID value) {
        this.value = Objects.requireNonNull(value, "TenantId value ne peut pas être null");
    }

    // -------------------- FACTORIES --------------------

    /**
     * Génère un nouveau TenantId.
     * Typiquement utilisé lors de l'onboarding d'un tenant.
     */
    public static TenantId generate() {
        return new TenantId(UUID.randomUUID());
    }

    /**
     * Reconstruit un TenantId existant.
     * (DB, event sourcing, messaging, API)
     */
    public static TenantId of(UUID value) {
        return new TenantId(value);
    }

    /**
     * Reconstruit un TenantId depuis une représentation String UUID.
     *
     * @throws IllegalArgumentException si le format est invalide
     */
    public static TenantId fromString(String raw) {
        Objects.requireNonNull(raw, "TenantId string ne peut pas être null");
        try {
            return new TenantId(UUID.fromString(raw));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Format TenantId invalide : " + raw, ex);
        }
    }

    // -------------------- ACCESSEURS --------------------

    public UUID value() {
        return value;
    }

    public String asString() {
        return value.toString();
    }

    // -------------------- MÉTIER --------------------

    /**
     * Vérifie si deux TenantId représentent le même tenant.
     */
    public boolean sameAs(TenantId other) {
        Objects.requireNonNull(other, "TenantId de comparaison ne peut pas être null");
        return this.value.equals(other.value);
    }

    // -------------------- COMPARABLE --------------------

    @Override
    public int compareTo(TenantId other) {
        return this.value.compareTo(other.value);
    }

    // -------------------- EQUALITY & HASHCODE --------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantId that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    // -------------------- TO STRING --------------------

    @Override
    public String toString() {
        return value.toString();
    }
}
