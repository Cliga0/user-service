package com.solvia.userservice.domain.model.vo.address;

import java.io.Serializable;

public final class Country implements Serializable {

    private final String code;

    private Country(String code) {
        this.code = code;
    }

    public static Country of(String code) {
        if (code == null || code.length() != 2) {
            throw new IllegalArgumentException("Country must be ISO-2 code");
        }

        return new Country(code.toUpperCase());
    }

    public String code() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
