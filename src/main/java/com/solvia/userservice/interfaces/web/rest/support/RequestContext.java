package com.solvia.userservice.interfaces.web.rest.support;

import com.solvia.userservice.domain.model.vo.metadata.ActorId;
import com.solvia.userservice.shared.TenantId;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * RequestContext
 * <p>
 * IMMUTABLE REQUEST SNAPSHOT
 */
public final class RequestContext implements Serializable {

    // =========================
    // ACTOR
    // =========================

    private final ActorId actorId;

    private final ActorType actorType;

    // =========================
    // OBSERVABILITY
    // =========================

    private final UUID correlationId;

    // =========================
    // TENANT
    // =========================

    private final TenantId tenantId;

    // =========================
    // CONSTRUCTOR
    // =========================

    private RequestContext(
            ActorId actorId,
            ActorType actorType,
            UUID correlationId,
            TenantId tenantId
    ) {
        this.actorId = Objects.requireNonNull(actorId);
        this.actorType = Objects.requireNonNull(actorType);
        this.correlationId = Objects.requireNonNull(correlationId);
        this.tenantId = Objects.requireNonNull(tenantId);
    }

    // =========================
    // FACTORY AUTHENTICATED
    // =========================

    public static RequestContext authenticated(
            ActorId actorId,
            UUID correlationId,
            TenantId tenantId
    ) {
        return new RequestContext(
                actorId,
                ActorType.AUTHENTICATED,
                correlationId,
                tenantId
        );
    }

    // =========================
    // FACTORY SYSTEM
    // =========================

    public static RequestContext system(UUID correlationId) {
        return new RequestContext(
                ActorId.system(),
                ActorType.SYSTEM,
                correlationId,
                TenantId.of(UUID.fromString("00000000-0000-0000-0000-000000000000"))
        );
    }

    // =========================
    // FACTORY ANONYMOUS
    // =========================

    public static RequestContext anonymous(UUID correlationId) {
        return new RequestContext(
                ActorId.of(UUID.fromString("00000000-0000-0000-0000-000000000000")),
                ActorType.ANONYMOUS,
                correlationId,
                TenantId.of(UUID.fromString("00000000-0000-0000-0000-000000000000"))
        );
    }

    // =========================
    // GETTERS
    // =========================

    public ActorId actorId() {
        return actorId;
    }

    public ActorType actorType() {
        return actorType;
    }

    public UUID correlationId() {
        return correlationId;
    }

    public TenantId tenantId() {
        return tenantId;
    }

    @Override
    public String toString() {
        return "RequestContext{" +
                "actorId=" + actorId +
                ", actorType=" + actorType +
                ", correlationId=" + correlationId +
                ", tenantId=" + tenantId +
                '}';
    }
}