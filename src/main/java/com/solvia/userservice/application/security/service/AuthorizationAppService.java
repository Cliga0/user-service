package com.solvia.userservice.application.security.service;

import com.solvia.userservice.application.security.context.AuthenticatedUser;
import com.solvia.userservice.domain.authorization.*;

import java.util.Objects;

/**
 * Application service pour l'autorisation.
 * Orchestration uniquement.
 */
public final class AuthorizationAppService {

    private final AuthorizationEngine engine;

    public AuthorizationAppService(AuthorizationEngine engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    public void check(
            AuthenticatedUser user,
            Action action,
            Resource resource
    ) {
        AuthorizationContext context = new AuthorizationContext(
                user.subject(),
                user.tenantId()
        );

        boolean allowed = engine.isAllowed(
                context,
                action,
                resource
        );

        if (!allowed) {
            throw new SecurityException(buildMessage(user, action, resource));
        }
    }

    private String buildMessage(
            AuthenticatedUser user,
            Action action,
            Resource resource
    ) {
        return "Access denied: user=" + user.userId().value()
                + ", action=" + action
                + ", resource=" + resource;
    }
}