package com.solvia.userservice.domain.model.vo.address;

import java.io.Serializable;

public final class PostalCode implements Serializable {

    private final String value;

    private PostalCode(String value) {
        this.value = value;
    }

    public static PostalCode of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("PostalCode invalid");
        }

        return new PostalCode(value.trim());
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
