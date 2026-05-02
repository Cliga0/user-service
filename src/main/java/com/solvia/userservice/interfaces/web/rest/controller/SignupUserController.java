package com.solvia.userservice.interfaces.web.rest.controller;

import com.solvia.userservice.application.command.signup.model.SignupUserCommand;
import com.solvia.userservice.application.command.signup.result.SignupUserResult;
import com.solvia.userservice.application.port.in.SignupUserUseCase;
import com.solvia.userservice.interfaces.web.rest.dto.request.SignupUserRequest;
import com.solvia.userservice.interfaces.web.rest.dto.response.SignupUserResponse;
import com.solvia.userservice.interfaces.web.rest.factory.SignupUserCommandFactory;
import com.solvia.userservice.interfaces.web.rest.mapper.SignupUserWebMapper;
import com.solvia.userservice.interfaces.web.rest.support.RequestContext;
import com.solvia.userservice.interfaces.web.rest.support.RequestContextResolver;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * SIGNUP CONTROLLER (HTTP EDGE ADAPTER)
 * <p>
 * RESPONSIBILITIES:
 * - HTTP request/response translation
 * - Delegation to application layer
 * <p>
 * STRICT RULES:
 * - no business logic
 * - no domain knowledge
 * - no infrastructure calls
 */
@RestController
@RequestMapping("/api/v1/auth")
public final class SignupUserController {

    private final SignupUserUseCase signupUserUseCase;
    private final SignupUserCommandFactory commandFactory;
    private final SignupUserWebMapper mapper;
    private final RequestContextResolver contextResolver;

    public SignupUserController(
            SignupUserUseCase signupUserUseCase,
            SignupUserCommandFactory commandFactory,
            SignupUserWebMapper mapper,
            RequestContextResolver contextResolver
    ) {
        this.signupUserUseCase = Objects.requireNonNull(signupUserUseCase);
        this.commandFactory = Objects.requireNonNull(commandFactory);
        this.mapper = Objects.requireNonNull(mapper);
        this.contextResolver = Objects.requireNonNull(contextResolver);
    }

    /**
     * Public user registration (Signup)
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupUserResponse> signup(
            @Valid @RequestBody SignupUserRequest request
    ) {

        // =====================================================
        // 1. REQUEST CONTEXT (observability / tracing)
        // =====================================================
        RequestContext context = contextResolver.resolve();

        // =====================================================
        // 2. HTTP → APPLICATION COMMAND
        // =====================================================
        SignupUserCommand command = commandFactory.create(request, context);

        // =====================================================
        // 3. APPLICATION USE CASE EXECUTION
        // =====================================================
        SignupUserResult result = signupUserUseCase.signup(command);

        // =====================================================
        // 4. DOMAIN → HTTP RESPONSE MAPPING
        // =====================================================
        SignupUserResponse response = mapper.toSignupResponse(result);

        // =====================================================
        // 5. HTTP RESPONSE
        // =====================================================
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}