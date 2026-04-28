package com.solvia.userservice.domain.model.vo.person;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Value Object représentant une date de naissance.
 *
 * <p>
 * Responsabilités :
 * - garantir une date valide
 * - empêcher les dates futures
 * - fournir calcul d'âge
 * </p>
 */
public final class BirthDate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDate value;

    private BirthDate(LocalDate value) {
        this.value = value;
    }

    /**
     * Factory method principale.
     */
    public static BirthDate of(LocalDate value) {
        Objects.requireNonNull(value, "BirthDate must not be null");

        validate(value);

        return new BirthDate(value);
    }

    /**
     * Validation de cohérence temporelle.
     */
    private static void validate(LocalDate value) {

        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("BirthDate cannot be in the future");
        }

        // protection contre dates absurdes
        if (value.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new IllegalArgumentException("BirthDate is too far in the past");
        }
    }

    /**
     * Retourne la valeur brute.
     */
    public LocalDate value() {
        return value;
    }

    /**
     * Calcule l'âge actuel.
     */
    public int age() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    /**
     * Vérifie si la personne est mineure/majeure (règle générique).
     */
    public boolean isAdult(int adultAge) {
        return age() >= adultAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BirthDate that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
