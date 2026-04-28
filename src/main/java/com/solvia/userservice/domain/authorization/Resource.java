package com.solvia.userservice.domain.authorization;

import java.util.Objects;

public final class Resource {

    private final ResourceType type;
    private final String id;

    private Resource(ResourceType type, String id) {
        this.type = Objects.requireNonNull(type, "ResourceType required");
        this.id = normalizeId(id);
    }

    public static Resource of(ResourceType type, String id) {
        return new Resource(type, id);
    }

    private String normalizeId(String id) {
        Objects.requireNonNull(id, "Resource id required");
        String v = id.trim();
        if (v.isEmpty()) {
            throw new IllegalArgumentException("Resource id cannot be empty");
        }
        return v;
    }

    public ResourceType type() {
        return type;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Resource that)) return false;
        return type.equals(that.type) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    @Override
    public String toString() {
        return type + ":" + id;
    }

    public String toKey() { return type + ":" + id; }
}