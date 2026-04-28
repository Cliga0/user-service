package com.solvia.userservice.infrastructure.configuration;

import com.solvia.userservice.application.port.out.auth.AuthenticatedUserProvider;
import com.solvia.userservice.interfaces.web.rest.factory.CreateUserCommandFactory;
import com.solvia.userservice.interfaces.web.rest.mapper.UserWebMapper;
import com.solvia.userservice.interfaces.web.rest.support.RequestContextResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfacesConfiguration {

    @Bean
    public UserWebMapper userWebMapper() {
        return new UserWebMapper();
    }

    @Bean
    public CreateUserCommandFactory createUserCommandFactory() {
        return new CreateUserCommandFactory();
    }

    @Bean
    public RequestContextResolver requestContextResolver(
            AuthenticatedUserProvider authUserProvider
    ) {
        return new RequestContextResolver(authUserProvider);
    }
}