package com.solvia.userservice.infrastructure.configuration;

import com.solvia.userservice.interfaces.web.rest.error.CorrelationIdProvider;
import com.solvia.userservice.interfaces.web.rest.error.DefaultCorrelationIdProvider;
import com.solvia.userservice.interfaces.web.rest.error.ErrorResponseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * SecurityConfiguration
 *
 * <p>
 * INFRASTRUCTURE CONFIGURATION
 *
 * <p>
 * RESPONSABILITÉS :
 * - Configurer Spring Security (HTTP + JWT)
 * - Brancher le decoder JWT
 * - Mapper JWT → Authentication Spring
 *
 * <p>
 * FAANG-grade principles:
 * - Stateless
 * - Fail-fast
 * - Extensible (multi-provider ready)
 * - No hardcoded values
 * - Defensive parsing
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String ROLE_PREFIX = "ROLE_";

    // ---------------------------------------------------------------------
    // SECURITY FILTER CHAIN
    // ---------------------------------------------------------------------

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwkSetUri("http://keycloak:8080/realms/userservice/protocol/openid-connect/certs")
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    // ---------------------------------------------------------------------
    // JWT → SPRING AUTHENTICATION (DDD-ALIGNED)
    // ---------------------------------------------------------------------

    @Bean
    Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {

        return jwt -> {

            var realmAccess = jwt.getClaimAsMap("realm_access");

            var roles = realmAccess != null
                    ? (List<String>) realmAccess.get("roles")
                    : List.<String>of();

            var authorities = roles.stream()
                    .map(r -> ROLE_PREFIX + r)
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    @Bean
    public ErrorResponseFactory errorResponseFactory(CorrelationIdProvider correlationIdProvider) {
        return new ErrorResponseFactory(correlationIdProvider);
    }

    @Bean
    public CorrelationIdProvider correlationIdProvider() {
        return new DefaultCorrelationIdProvider();
    }
}
