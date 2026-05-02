package com.solvia.userservice.infrastructure.configuration;

import com.solvia.userservice.application.command.signup.handler.SignupUserHandler;
import com.solvia.userservice.application.command.signup.result.SignupUserResultMapper;
import com.solvia.userservice.application.port.in.SignupUserUseCase;
import com.solvia.userservice.application.port.out.UserRepository;
import com.solvia.userservice.application.port.out.auth.AuthenticatedUserProvider;
import com.solvia.userservice.application.port.out.event.EventPublisher;
import com.solvia.userservice.application.security.context.AuthenticatedUser;
import com.solvia.userservice.domain.model.factory.UserFactory;

import com.solvia.userservice.domain.model.vo.business.UserRole;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.shared.TenantId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfiguration {

    // =====================================================
    // SIGNUP USER (MISSING PART → FIX)
    // =====================================================

    @Bean
    public SignupUserUseCase signupUserUseCase(
            UserRepository userRepository,
            EventPublisher eventPublisher,
            UserFactory userFactory,
            SignupUserResultMapper resultMapper
    ) {
        return new SignupUserHandler(
                userRepository,
                eventPublisher,
                userFactory,
                resultMapper
        );
    }

    // =====================================================
    // AUTHENTICATED USER PROVIDER
    // =====================================================

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

            return AuthenticatedUser.of(
                            UserId.fromString(jwt.getSubject()),
                            TenantId.fromString(jwt.getClaimAsString("org_id")),
                            mappedRoles

            );
        };
    }

    // =====================================================
    // MAPPERS
    // =====================================================

    @Bean
    public SignupUserResultMapper signupUserResultMapper() {
        return new SignupUserResultMapper();
    }
}