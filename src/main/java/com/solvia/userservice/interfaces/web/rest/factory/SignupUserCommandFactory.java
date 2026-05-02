package com.solvia.userservice.interfaces.web.rest.factory;

import com.solvia.userservice.application.command.signup.model.SignupUserCommand;
import com.solvia.userservice.interfaces.web.rest.dto.request.SignupUserRequest;
import com.solvia.userservice.interfaces.web.rest.support.RequestContext;

import java.util.Objects;

/**
 * SignupUserCommandFactory
 * <p>
 * RESPONSIBILITY:
 * - Translate HTTP layer into Application Command
 * - Normalize raw transport values
 * <p>
 * STRICT RULES:
 * - no business logic
 * - no domain knowledge
 * - no persistence calls
 */
public final class SignupUserCommandFactory {

    /**
     * Build SignupUserCommand from HTTP request + request context
     */
    public SignupUserCommand create(
            SignupUserRequest request,
            RequestContext context
    ) {

        Objects.requireNonNull(request, "SignupUserRequest must not be null");
        Objects.requireNonNull(context, "RequestContext must not be null");

        return new SignupUserCommand(
                trim(request.email()),
                trim(request.phoneNumber()),
                trim(request.firstName()),
                trim(request.lastName()),
                request.password(),
                trim(request.externalAuthId()),
                trim(request.gender()),
                request.birthDate()
        );
    }

    // =====================================================
    // NORMALIZATION HELPERS
    // =====================================================

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}