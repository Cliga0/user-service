package com.solvia.userservice.interfaces.web.rest.mapper;

import com.solvia.userservice.application.command.result.CreateUserResult;
import com.solvia.userservice.interfaces.web.rest.dto.response.CreateUserResponse;

import java.util.Objects;

/**
 * UserWebMapper
 * <p>
 * PURE TRANSFORMATION LAYER
 * <p>
 * RESPONSIBILITIES:
 * - HTTP DTO → Application Command
 * - Application Result → HTTP Response
 * <p>
 * NON RESPONSIBILITIES:
 * - no ID generation
 * - no security context
 * - no orchestration
 */
public final class UserWebMapper {

    // =========================
    // RESULT → RESPONSE
    // =========================

    public CreateUserResponse toResponse(CreateUserResult result) {

        Objects.requireNonNull(result, "result must not be null");

        return new CreateUserResponse(
                result.userId(),
                result.email(),
                result.role(),
                result.created(),
                result.createdAt(),
                result.self()
        );
    }
}