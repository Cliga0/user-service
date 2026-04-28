package com.solvia.userservice.domain.model.vo.address;

import java.io.Serializable;

public final class Street implements Serializable {

    private final String value;

    private Street(String value) {
        this.value = value;
    }

    public static Street of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }

        return new Street(value.trim());
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
