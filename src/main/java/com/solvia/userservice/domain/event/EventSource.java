package com.solvia.userservice.domain.event;

import java.io.Serializable;
import java.util.Objects;

/**
 * EventSource
 * <p>
 * Représente la source d’un événement :
 * service + version.
 */
public final class EventSource implements Serializable {

    private final String service;
    private final String version;

    private EventSource(String service, String version) {
        this.service = service;
        this.version = version;
    }

    public static EventSource of(String service, String version) {
        Objects.requireNonNull(service);
        Objects.requireNonNull(version);

        if (service.isBlank() || version.isBlank()) {
            throw new IllegalArgumentException("Service and version cannot be blank");
        }

        return new EventSource(service, version);
    }

    public String service() {
        return service;
    }

    public String version() {
        return version;
    }

    public String value() {
        return service + ":" + version;
    }

    @Override
    public String toString() {
        return value();
    }
}
