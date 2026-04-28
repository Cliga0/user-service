package com.solvia.userservice.interfaces.web.rest.error;

import java.util.UUID;

public class DefaultCorrelationIdProvider implements CorrelationIdProvider {

    @Override
    public UUID get() {
        return UUID.randomUUID();
    }
}