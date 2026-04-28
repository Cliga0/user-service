package com.solvia.userservice.application.security.context;

import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.shared.TenantId;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class JwtContext {

    private final UserId userId;
    private final TenantId tenantId;
    private final Set<SecurityRole> roles;

    private JwtContext(Builder builder) {
        this.userId = builder.userId;
        this.tenantId = builder.tenantId;
        this.roles = Set.copyOf(builder.roles);
    }

    public UserId userId() {
        return userId;
    }

    public TenantId tenantId() {
        return tenantId;
    }

    public Set<SecurityRole> roles() {
        return roles;
    }

    // =========================
    // BUILDER (clean + extensible)
    // =========================

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UserId userId;
        private TenantId tenantId;
        private Set<SecurityRole> roles = Collections.emptySet();

        public Builder userId(UserId userId) {
            this.userId = Objects.requireNonNull(userId);
            return this;
        }

        public Builder tenantId(TenantId tenantId) {
            this.tenantId = Objects.requireNonNull(tenantId);
            return this;
        }

        public Builder roles(Set<SecurityRole> roles) {
            this.roles = Objects.requireNonNull(roles);
            return this;
        }

        public JwtContext build() {
            Objects.requireNonNull(userId);
            Objects.requireNonNull(tenantId);

            if (roles == null || roles.isEmpty()) {
                throw new IllegalArgumentException("roles cannot be empty");
            }

            return new JwtContext(this);
        }
    }
}