package com.solvia.userservice.interfaces.web.rest.support;

import com.solvia.userservice.application.port.out.auth.AuthenticatedUserProvider;
import com.solvia.userservice.application.security.context.AuthenticatedUser;
import com.solvia.userservice.domain.model.vo.metadata.SystemActors;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * RequestContextResolver
 * <p>
 * EDGE CONTEXT BUILDER
 * <p>
 * RESPONSIBILITIES:
 * - Build deterministic request context
 * - Aggregate security + request metadata
 * <p>
 * NON RESPONSIBILITIES:
 * - no business logic
 * - no domain mapping
 */
public final class RequestContextResolver {

    private final AuthenticatedUserProvider authUserProvider;

    public RequestContextResolver(AuthenticatedUserProvider authUserProvider) {
        this.authUserProvider = Objects.requireNonNull(authUserProvider);
    }

    public RequestContext resolve() {

        Optional<AuthenticatedUser> userOpt = authUserProvider.current();

        UUID actorId = userOpt
                .map(u -> u.userId().value())
                .orElseGet(this::systemActorId);

        String tenantId = userOpt
                .map(this::resolveTenantId)
                .orElse("default-tenant");

        UUID correlationId = resolveCorrelationId();

        return RequestContext.of(
                actorId,
                correlationId,
                tenantId
        );
    }

    // =====================================================
    // SYSTEM ACTOR (CRITICAL)
    // =====================================================

    private UUID systemActorId() {
        return SystemActors.SYSTEM.value();
    }

    // =====================================================
    // CORRELATION ID (OBSERVABILITY)
    // =====================================================

    private UUID resolveCorrelationId() {
        return UUID.randomUUID(); // next step: MDC / header propagation
    }

    // =====================================================
    // TENANT RESOLUTION
    // =====================================================

    private String resolveTenantId(AuthenticatedUser user) {
        return "default-tenant";
    }
}