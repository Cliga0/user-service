package com.solvia.userservice.interfaces.web.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * SignupUserRequest
 * <p>
 * HTTP DTO (EDGE LAYER ONLY)
 * <p>
 * RESPONSIBILITY:
 * - Carry raw input data from client
 * <p>
 * STRICT RULES:
 * - no domain concepts
 * - no business logic
 * - validation only at syntactic level
 */
public record SignupUserRequest(

        @NotBlank(message = "externalAuthId is required")
        String externalAuthId,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,

        @NotBlank(message = "firstName is required")
        @Size(min = 2, max = 50)
        String firstName,

        @NotBlank(message = "lastName is required")
        @Size(min = 2, max = 50)
        String lastName,

        @NotBlank(message = "password is required")
        @Size(min = 8, max = 100)
        String password,

        @NotNull
        LocalDate birthDate,

        @NotBlank
        String gender,

        @NotBlank
        String phoneNumber
) {}