package com.solvia.userservice.infrastructure.persistence.user.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * AddressEmbeddable
 *
 * <p>
 * Représentation persistence d'une adresse.
 *
 * <p>
 * ⚠️ Aucun comportement métier
 * ⚠️ Strictement technique (JPA)
 */
@Embeddable
public class AddressEmbeddable implements Serializable {

    private String street;
    private String city;
    private String country;
    private String postalCode;

    protected AddressEmbeddable() {
        // JPA only
    }

    public AddressEmbeddable(
            String street,
            String city,
            String country,
            String postalCode
    ) {
        this.street = Objects.requireNonNull(street);
        this.city = Objects.requireNonNull(city);
        this.country = Objects.requireNonNull(country);
        this.postalCode = Objects.requireNonNull(postalCode);
    }

    // =========================
    // GETTERS
    // =========================

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getPostalCode() { return postalCode; }
}
