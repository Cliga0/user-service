package com.solvia.userservice.infrastructure.configuration;

import com.solvia.userservice.domain.authorization.AuthorizationEngine;
import com.solvia.userservice.domain.authorization.AuthorizationRule;
import com.solvia.userservice.domain.authorization.TupleRepository;
import com.solvia.userservice.domain.authorization.rules.AdminRule;
import com.solvia.userservice.domain.model.factory.UserFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainConfiguration {

    @Bean
    public UserFactory userFactory() {
        return new UserFactory();
    }

    @Bean
    public AuthorizationEngine authorizationEngine(
            List<AuthorizationRule> rules,
            TupleRepository tupleRepository
    ) {
        return new AuthorizationEngine(rules, tupleRepository);
    }

    @Bean
    public AuthorizationRule adminRule() {
        return new AdminRule();
    }
}
