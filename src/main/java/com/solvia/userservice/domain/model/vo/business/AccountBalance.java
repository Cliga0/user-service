package com.solvia.userservice.domain.model.vo.business;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object représentant un solde de compte utilisateur.
 *
 * <p>
 * - Immutable
 * - Précision financière (BigDecimal)
 * - Opérations contrôlées
 * </p>
 */
public final class AccountBalance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final BigDecimal value;

    private AccountBalance(BigDecimal value) {
        this.value = value;
    }

    /**
     * Factory method principale.
     */
    public static AccountBalance of(BigDecimal value) {
        Objects.requireNonNull(value, "Balance must not be null");

        BigDecimal normalized = normalize(value);

        validate(normalized);

        return new AccountBalance(normalized);
    }

    /**
     * Solde initial.
     */
    public static AccountBalance zero() {
        return new AccountBalance(ZERO);
    }

    /**
     * Normalisation (scale uniforme).
     */
    private static BigDecimal normalize(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Validation métier.
     */
    private static void validate(BigDecimal value) {
        if (value.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("AccountBalance cannot be negative");
        }
    }

    /**
     * Ajout d'argent (immutable).
     */
    public AccountBalance add(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null");

        BigDecimal result = this.value.add(amount);

        return new AccountBalance(normalize(result));
    }

    /**
     * Retrait d'argent (avec protection).
     */
    public AccountBalance subtract(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null");

        BigDecimal result = this.value.subtract(amount);

        if (result.compareTo(ZERO) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        return new AccountBalance(normalize(result));
    }

    /**
     * Valeur brute.
     */
    public BigDecimal value() {
        return value;
    }

    /**
     * Vérifie suffisance de fonds.
     */
    public boolean isGreaterOrEqual(BigDecimal amount) {
        return this.value.compareTo(amount) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountBalance that)) return false;
        return value.compareTo(that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
