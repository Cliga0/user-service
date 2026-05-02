package com.solvia.userservice.infrastructure.configuration;

import com.solvia.userservice.application.port.out.auth.AuthenticatedUserProvider;
import com.solvia.userservice.interfaces.web.rest.factory.SignupUserCommandFactory;
import com.solvia.userservice.interfaces.web.rest.mapper.SignupUserWebMapper;
import com.solvia.userservice.interfaces.web.rest.support.RequestContextResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfacesConfiguration {

    // =====================================================
    // MAPPERS
    // =====================================================

    @Bean
    public SignupUserWebMapper signupUserWebMapper() {
        return new SignupUserWebMapper();
    }

    // =====================================================
    // COMMAND FACTORIES
    // =====================================================

    @Bean
    public SignupUserCommandFactory signupUserCommandFactory() {
        return new SignupUserCommandFactory();
    }

    // =====================================================
    // CONTEXT RESOLVER
    // =====================================================

    @Bean
    public RequestContextResolver requestContextResolver(
            AuthenticatedUserProvider authUserProvider
    ) {
        return new RequestContextResolver(authUserProvider);
    }
}