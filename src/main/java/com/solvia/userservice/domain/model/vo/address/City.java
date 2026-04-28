package com.solvia.userservice.domain.model.vo.address;

import java.io.Serializable;

public final class City implements Serializable {

    private final String value;

    private City(String value) {
        this.value = value;
    }

    public static City of(String value) {
        if (value == null || value.trim().length() < 2) {
            throw new IllegalArgumentException("City invalid");
        }

        return new City(value.trim());
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
