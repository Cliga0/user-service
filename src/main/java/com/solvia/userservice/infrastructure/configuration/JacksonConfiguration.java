package com.solvia.userservice.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        // Support Java Time API (Instant, LocalDate, etc.)
        mapper.registerModule(new JavaTimeModule());

        // Force ISO-8601 au lieu de timestamps bruts
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Auto-detection des modules supplémentaires (safe extension)
        mapper.findAndRegisterModules();

        return mapper;
    }
}