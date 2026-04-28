package com.solvia.userservice.interfaces.web.rest.factory;

import com.solvia.userservice.application.command.model.CreateUserCommand;
import com.solvia.userservice.interfaces.web.rest.dto.request.CreateUserRequest;
import com.solvia.userservice.interfaces.web.rest.support.RequestContext;

import java.util.Objects;

/**
 * CreateUserCommandFactory
 * <p>
 * PURE TRANSFORMATION LAYER (NO SIDE EFFECTS)
 * <p>
 * RESPONSIBILITY:
 * - Transform HTTP layer input into Application Command
 * <p>
 * NON RESPONSIBILITY:
 * - no context resolution
 * - no ID generation
 * - no security access
 * - no business logic
 */
public final class CreateUserCommandFactory {

    public CreateUserCommand create(
            CreateUserRequest request,
            RequestContext context
    ) {

        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(context, "context must not be null");

        return new CreateUserCommand(
                null,
                request.externalAuthId(),
                request.firstName(),
                request.lastName(),
                request.birthDate(),
                request.gender(),
                normalizeEmail(request.email()),
                normalize(request.phoneNumber()),
                normalize(request.role()),
                context.actorId()
        );
    }

    // =========================
    // PURE NORMALIZATION
    // =========================

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeEmail(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }
}