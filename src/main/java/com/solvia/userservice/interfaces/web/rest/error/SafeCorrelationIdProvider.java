package com.solvia.userservice.interfaces.web.rest.error;

import com.solvia.userservice.interfaces.web.rest.support.RequestContextResolver;

import java.util.UUID;

public class SafeCorrelationIdProvider implements CorrelationIdProvider {

    private final RequestContextResolver contextResolver;

    public SafeCorrelationIdProvider(RequestContextResolver contextResolver) {
        this.contextResolver = contextResolver;
    }

    @Override
    public UUID get() {
        try {
            return contextResolver.resolve().correlationId();
        } catch (Exception e) {
            return UUID.randomUUID(); // HARD fallback
        }
    }
}