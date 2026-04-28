package com.solvia.userservice.domain.authorization;

import java.util.Objects;

public final class Action {

    private final String value;

    private Action(String value) {
        this.value = normalize(value);
    }

    public static Action of(String value) {
        return new Action(value);
    }

    private String normalize(String value) {
        Objects.requireNonNull(value, "Action cannot be null");
        String v = value.trim().toUpperCase();
        if (v.isEmpty()) {
            throw new IllegalArgumentException("Action cannot be empty");
        }
        return v;
    }

    public String value() {
        return value;
    }

    public boolean isRead() { return value.equals("USER_READ"); }

    @Override
    public boolean equals(Object o) {
        return o instanceof Action a && value.equals(a.value);
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