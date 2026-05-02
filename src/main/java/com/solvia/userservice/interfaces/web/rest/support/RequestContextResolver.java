package com.solvia.userservice.interfaces.web.rest.support;

import com.solvia.userservice.application.port.out.auth.AuthenticatedUserProvider;
import com.solvia.userservice.application.security.context.AuthenticatedUser;
import com.solvia.userservice.domain.model.vo.metadata.ActorId;
import com.solvia.userservice.shared.TenantId;

import java.util.Objects;
import java.util.UUID;

/**
 * RequestContextResolver
 * <p>
 * EDGE CONTEXT BUILDER — FAANG GRADE
 * <p>
 * RESPONSIBILITIES:
 * - Build deterministic request context
 * - Aggregate security + request metadata
 * - Provide SAFE fallback (never break request flow)
 * <p>
 * DESIGN:
 * - DDD compliant
 * - Fail-safe for public endpoints
 * - Ready for multi-tenant & distributed tracing
 */
public final class RequestContextResolver {

    private final AuthenticatedUserProvider authUserProvider;

    public RequestContextResolver(AuthenticatedUserProvider authUserProvider) {
        this.authUserProvider = Objects.requireNonNull(authUserProvider);
    }

    // =====================================================
    // ENTRY POINT
    // =====================================================

    public RequestContext resolve() {

        AuthenticatedUser user = safeUser();

        UUID correlationId = resolveCorrelationId();

        if (user == null) {
            return RequestContext.anonymous(correlationId);
        }

        return buildAuthenticated(user, correlationId);
    }

    // =====================================================
    // SAFE USER RESOLUTION
    // =====================================================

    private AuthenticatedUser safeUser() {
        try {
            return authUserProvider.current();
        } catch (Exception ex) {
            return null;
        }
    }

    // =====================================================
    // AUTHENTICATED FLOW
    // =====================================================

    private RequestContext buildAuthenticated(
            AuthenticatedUser user,
            UUID correlationId
    ) {
        ActorId actorId = ActorId.of(user.userId());

        TenantId tenantId = resolveTenant(user);

        return RequestContext.authenticated(
                actorId,
                correlationId,
                tenantId
        );
    }

    // =====================================================
    // TENANT RESOLUTION (EXTENSIBLE)
    // =====================================================

    private TenantId resolveTenant(AuthenticatedUser user) {
        return user.tenantId();
    }

    // =====================================================
    // CORRELATION ID (OBSERVABILITY)
    // =====================================================

    private UUID resolveCorrelationId() {
        return UUID.randomUUID();
    }
}