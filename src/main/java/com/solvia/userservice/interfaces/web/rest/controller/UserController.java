package com.solvia.userservice.interfaces.web.rest.controller;

import com.solvia.userservice.application.command.model.CreateUserCommand;
import com.solvia.userservice.application.command.result.CreateUserResult;
import com.solvia.userservice.application.port.in.CreateUserUseCase;
import com.solvia.userservice.interfaces.web.rest.dto.request.CreateUserRequest;
import com.solvia.userservice.interfaces.web.rest.dto.response.CreateUserResponse;
import com.solvia.userservice.interfaces.web.rest.mapper.UserWebMapper;
import com.solvia.userservice.interfaces.web.rest.factory.CreateUserCommandFactory;

import com.solvia.userservice.interfaces.web.rest.support.RequestContext;
import com.solvia.userservice.interfaces.web.rest.support.RequestContextResolver;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * UserController
 * <p>
 * HTTP EDGE ADAPTER (PURE)
 * <p>
 * RESPONSIBILITIES:
 * - HTTP translation only
 * - delegate orchestration to factory/use case
 * <p>
 * NON RESPONSIBILITIES:
 * - no business logic
 * - no context logic
 * - no ID generation
 */
@RestController
@RequestMapping("/api/v1/users")
public final class UserController {

    private final CreateUserUseCase userUseCase;
    private final UserWebMapper mapper;
    private final CreateUserCommandFactory commandFactory;
    private final RequestContextResolver contextResolver;

    public UserController(
            CreateUserUseCase userUseCase,
            UserWebMapper mapper,
            CreateUserCommandFactory commandFactory,
            RequestContextResolver contextResolver
    ) {
        this.userUseCase = Objects.requireNonNull(userUseCase);
        this.mapper = Objects.requireNonNull(mapper);
        this.commandFactory = Objects.requireNonNull(commandFactory);
        this.contextResolver = Objects.requireNonNull(contextResolver);
    }

    /**
     * Create a new user
     */
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {

        // =========================
        // 1. RESOLVE EXECUTION CONTEXT
        // =========================
        RequestContext context = contextResolver.resolve();

        // =========================
        // 1. COMMAND BUILDING
        // =========================
        CreateUserCommand command = commandFactory.create(request, context);

        // =========================
        // 2. USE CASE EXECUTION
        // =========================
        CreateUserResult result = userUseCase.createUser(command);

        // =========================
        // 3. RESPONSE MAPPING
        // =========================
        CreateUserResponse response = mapper.toResponse(result);

        // =====================================
        // 4. HTTP RESPONSE
        // =====================================
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}