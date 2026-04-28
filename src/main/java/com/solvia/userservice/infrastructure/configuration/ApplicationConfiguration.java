package com.solvia.userservice.infrastructure.configuration;

import com.solvia.userservice.application.command.handler.CreateUserHandler;
import com.solvia.userservice.application.port.in.CreateUserUseCase;
import com.solvia.userservice.application.port.out.UserRepository;
import com.solvia.userservice.application.port.out.auth.AuthenticatedUserProvider;
import com.solvia.userservice.application.port.out.event.EventPublisher;
import com.solvia.userservice.application.security.context.AuthenticatedUser;
import com.solvia.userservice.application.security.service.AuthorizationAppService;
import com.solvia.userservice.application.command.result.CreateUserResultMapper;
import com.solvia.userservice.domain.authorization.AuthorizationEngine;
import com.solvia.userservice.domain.model.factory.UserFactory;

import com.solvia.userservice.domain.model.vo.business.UserRole;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.shared.TenantId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepository userRepository,
            EventPublisher eventPublisher,
            UserFactory userFactory,
            AuthenticatedUserProvider authUserProvider,
            AuthorizationAppService authorizationService,
            CreateUserResultMapper resultMapper
    ) {
        return new CreateUserHandler(
                userRepository,
                eventPublisher,
                userFactory,
                authUserProvider,
                authorizationService,
                resultMapper
        );
    }

    @Bean
    public AuthenticatedUserProvider authenticatedUserProvider() {

        return () -> {

            var auth = SecurityContextHolder.getContext().getAuthentication();

            if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
                throw new IllegalStateException("No JWT authentication found");
            }

            var jwt = jwtAuth.getToken();

            var realmAccess = jwt.getClaimAsMap("realm_access");

            var roles = realmAccess != null
                    ? (java.util.List<String>) realmAccess.get("roles")
                    : java.util.List.<String>of();

            Set<UserRole> mappedRoles = roles.stream()
                    .map(UserRole.Code::from)
                    .map(UserRole::of)
                    .collect(Collectors.toSet());

            return Optional.of(
                    AuthenticatedUser.of(
                            UserId.fromString(jwt.getSubject()),
                            TenantId.fromString(jwt.getClaimAsString("org_id")),
                            mappedRoles
                    )
            );
        };
    }

    @Bean
    public AuthorizationAppService authorizationAppService(
            AuthorizationEngine engine
    ) {
        return new AuthorizationAppService(engine);
    }

    @Bean
    public CreateUserResultMapper createUserResultMapper() {
        return new CreateUserResultMapper();
    }
}