package com.solvia.userservice.application.port.out.event;

import com.solvia.userservice.domain.event.DomainEvent;

import java.util.Collection;

public interface EventPublisher {
    void publish(Collection<DomainEvent> events);
}
