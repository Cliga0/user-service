package com.solvia.userservice.infrastructure.messaging.kafka;

import com.solvia.userservice.shared.events.IntegrationEvent;

/**
 * Exception levée lorsque la publication Kafka échoue.
 */
public class IntegrationEventPublishingException extends RuntimeException {

    private final IntegrationEvent event;

    public IntegrationEventPublishingException(
            IntegrationEvent event,
            Throwable cause
    ) {
        super("Failed to publish IntegrationEvent " + event.eventId(), cause);
        this.event = event;
    }

    public IntegrationEvent event() {
        return event;
    }
}
