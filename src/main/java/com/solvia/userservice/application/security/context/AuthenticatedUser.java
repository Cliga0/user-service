package com.solvia.userservice.application.security.context;

import com.solvia.userservice.domain.model.vo.business.UserRole;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.shared.TenantId;

import java.util.Objects;
import java.util.Set;

/**
 * Représente l'utilisateur authentifié dans l'application.
 * Ne contient PAS de logique métier.
 */
public final class AuthenticatedUser {

    private final UserId userId;
    private final TenantId tenantId;
    private final Set<UserRole> roles;

    private AuthenticatedUser(
            UserId userId,
            TenantId tenantId,
            Set<UserRole> roles
    ) {
        this.userId = Objects.requireNonNull(userId);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.roles = Set.copyOf(roles);

        if (roles.isEmpty()) {
            throw new IllegalStateException("User must have at least one role");
        }
    }

    public static AuthenticatedUser of(
            UserId userId,
            TenantId tenantId,
            Set<UserRole> roles
    ) {
        return new AuthenticatedUser(userId, tenantId, roles);
    }

    public String subject() {
        return "user:" + userId.asString();
    }

    public UserId userId() {
        return userId;
    }

    public TenantId tenantId() {
        return tenantId;
    }

    public Set<UserRole> roles() {
        return roles;
    }
}