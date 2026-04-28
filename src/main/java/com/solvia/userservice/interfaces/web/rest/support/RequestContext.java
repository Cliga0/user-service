package com.solvia.userservice.interfaces.web.rest.support;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * RequestContext
 * <p>
 * IMMUTABLE REQUEST SNAPSHOT
 * <p>
 * RESPONSIBILITIES:
 * - carry request identity metadata
 * - provide observability context
 * - define execution boundary identity
 * <p>
 * NON RESPONSIBILITIES:
 * - no business logic
 * - no framework dependency
 * - no lazy resolution
 */
public final class RequestContext implements Serializable {

    // =========================
    // CORE IDENTITY (MANDATORY)
    // =========================

    private final UUID actorId;

    // =========================
    // OBSERVABILITY (ALWAYS PRESENT)
    // =========================

    private final UUID correlationId;

    // =========================
    // MULTI-TENANCY (OPTIONAL BUT NORMALIZED)
    // =========================

    private final String tenantId;

    // =========================
    // CONSTRUCTOR (STRICT)
    // =========================

    private RequestContext(
            UUID actorId,
            UUID correlationId,
            String tenantId
    ) {
        this.actorId = Objects.requireNonNull(actorId, "actorId must not be null");
        this.correlationId = Objects.requireNonNull(correlationId, "correlationId must not be null");
        this.tenantId = tenantId;
    }

    // =========================
    // FACTORY (SINGLE ENTRY POINT)
    // =========================

    public static RequestContext of(
            UUID actorId,
            UUID correlationId,
            String tenantId
    ) {
        return new RequestContext(
                actorId,
                correlationId,
                normalizeTenant(tenantId)
        );
    }

    // =========================
    // DEFAULT FACTORY (SAFE)
    // =========================

    public static RequestContext of(UUID actorId, UUID correlationId) {
        return of(actorId, correlationId, null);
    }

    // =========================
    // NORMALIZATION
    // =========================

    private static String normalizeTenant(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return "default-tenant";
        }
        return tenantId.trim();
    }

    // =========================
    // ACCESSORS
    // =========================

    public UUID actorId() { return actorId; }

    public UUID correlationId() {
        return correlationId;
    }

    public String tenantId() {
        return tenantId;
    }

    // =========================
    // DEBUG / OBSERVABILITY
    // =========================

    @Override
    public String toString() {
        return "RequestContext{" +
                "actorId=" + actorId +
                ", correlationId=" + correlationId +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}