package com.solvia.userservice.infrastructure.configuration;

import com.solvia.userservice.application.port.out.event.EventPublisher;
import com.solvia.userservice.infrastructure.messaging.outbox.DomainEventMapper;
import com.solvia.userservice.infrastructure.messaging.outbox.DomainEventMapperImpl;
import com.solvia.userservice.infrastructure.messaging.outbox.OutboxDomainEventPublisher;
import com.solvia.userservice.infrastructure.messaging.outbox.repository.OutboxRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MessagingConfiguration {

    @Bean
    public EventPublisher eventPublisher(
            OutboxRepository repository,
            DomainEventMapper mapper
    ) {
        return new OutboxDomainEventPublisher(repository, mapper);
    }

    @Bean
    public DomainEventMapper domainEventMapper(ObjectMapper objectMapper) {
        return new DomainEventMapperImpl(objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules();
    }
}