package com.solvia.userservice.interfaces.web.rest.mapper;

import com.solvia.userservice.application.command.signup.result.SignupUserResult;
import com.solvia.userservice.interfaces.web.rest.dto.response.SignupUserResponse;

import java.util.Objects;

/**
 * SignupUserWebMapper
 * <p>
 * PURE PRESENTATION MAPPING LAYER
 * <p>
 * RESPONSIBILITY:
 * - map application result → HTTP response
 * <p>
 * STRICT RULES:
 * - no business logic
 * - no orchestration
 * - no dependency on other use cases
 */
public final class SignupUserWebMapper {

    /**
     * Map SignupUserResult → SignupUserResponse
     */
    public SignupUserResponse toSignupResponse(SignupUserResult result) {

        Objects.requireNonNull(result, "SignupUserResult must not be null");

        return new SignupUserResponse(
                result.userId(),
                result.email(),
                result.firstName(),
                result.lastName(),
                result.role(),
                result.createdAt(),
                result.activated()
        );
    }
}