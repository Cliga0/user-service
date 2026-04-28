package com.solvia.userservice.application.query.handler;

import com.solvia.userservice.application.exception.UnauthenticatedException;
import com.solvia.userservice.application.port.out.UserRepository;
import com.solvia.userservice.application.port.out.auth.AuthenticatedUserProvider;
import com.solvia.userservice.application.query.exception.UserNotFoundException;
import com.solvia.userservice.application.query.mapper.GetUserResultMapper;
import com.solvia.userservice.application.security.intent.AuthorizationIntent;
import com.solvia.userservice.application.security.service.AuthorizationAppService;
import com.solvia.userservice.application.security.context.AuthenticatedUser;
import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.application.query.model.GetUserQuery;
import com.solvia.userservice.application.query.result.GetUserResult;

import java.util.Objects;

public final class GetUserHandler {

    private final UserRepository userRepository;
    private final AuthenticatedUserProvider authUserProvider;
    private final AuthorizationAppService authorizationService;
    private final GetUserResultMapper mapper;

    public GetUserHandler(
            UserRepository userRepository,
            AuthenticatedUserProvider authUserProvider,
            AuthorizationAppService authorizationService,
            GetUserResultMapper mapper
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.authUserProvider = Objects.requireNonNull(authUserProvider);
        this.authorizationService = Objects.requireNonNull(authorizationService);
        this.mapper = Objects.requireNonNull(mapper);
    }

    public GetUserResult handle(GetUserQuery query) {

        Objects.requireNonNull(query, "GetUserQuery must not be null");

        // =========================
        // 1. AUTH USER
        // =========================
        AuthenticatedUser authUser = authUserProvider.current()
                .orElseThrow(UnauthenticatedException::new);

        // =========================
        // 2. AUTHORIZATION (READ)
        // =========================
        authorizationService.check(
                authUser,
                AuthorizationIntent.USER_READ,
                AuthorizationIntent.user(query.userId().asString())
        );

        // =========================
        // 3. LOAD USER (READ SIDE)
        // =========================
        User user = userRepository.findById(query.userId())
                .orElseThrow(() ->
                        new UserNotFoundException(query.userId())
                );

        // =========================
        // 4. DOMAIN → READ MODEL
        // =========================
        return mapper.toResult(user);
    }
}