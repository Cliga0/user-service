package com.solvia.userservice.infrastructure.messaging.outbox;

public enum OutboxEventStatus {
    PENDING,
    PUBLISHED,
    FAILED
}
