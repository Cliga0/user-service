package com.solvia.userservice.interfaces.web.rest.error;

import java.util.UUID;

public interface CorrelationIdProvider {
    UUID get();
}