package com.solvia.userservice.domain.model.vo.address;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object représentant une adresse complète.
 *
 * <p>
 * - Immutable
 * - Composite (Street, City, PostalCode, Country)
 * - Validé
 * </p>
 */
public final class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Street street;
    private final City city;
    private final PostalCode postalCode;
    private final Country country;

    private Address(Street street, City city, PostalCode postalCode, Country country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    /**
     * Factory method principale.
     */
    public static Address of(Street street, City city, PostalCode postalCode, Country country) {

        Objects.requireNonNull(street, "Street must not be null");
        Objects.requireNonNull(city, "City must not be null");
        Objects.requireNonNull(postalCode, "PostalCode must not be null");
        Objects.requireNonNull(country, "Country must not be null");

        return new Address(street, city, postalCode, country);
    }

    public Street street() {
        return street;
    }

    public City city() {
        return city;
    }

    public PostalCode postalCode() {
        return postalCode;
    }

    public Country country() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return street.equals(address.street)
            && city.equals(address.city)
            && postalCode.equals(address.postalCode)
            && country.equals(address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, postalCode, country);
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + postalCode + ", " + country;
    }
}
