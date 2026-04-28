package com.solvia.userservice.domain.authorization;

import java.util.Objects;

public final class ResourceType {

    private final String value;

    private ResourceType(String value) {
        this.value = normalize(value);
    }

    public static ResourceType of(String value) {
        return new ResourceType(value);
    }

    private String normalize(String value) {
        Objects.requireNonNull(value, "ResourceType cannot be null");
        String v = value.trim().toUpperCase();
        if (v.isEmpty()) {
            throw new IllegalArgumentException("ResourceType cannot be empty");
        }
        return v;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ResourceType that && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}