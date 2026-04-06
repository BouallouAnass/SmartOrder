package com.smartorder.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public final class Money {

    private final BigDecimal amount;
    private final Currency currency;

    // Private constructor — you control how Money is created
    private Money(BigDecimal amount, Currency currency) {
        if (amount == null) throw new IllegalArgumentException("Amount cannot be null");
        if (currency == null) throw new IllegalArgumentException("Currency cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new DomainException("Money amount cannot be negative: " + amount);

        // Always store with 4 decimal places, HALF_UP rounding
        // This ensures €10 and €10.0000 are treated identically
        this.amount = amount.setScale(4, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    // Static factory methods — more readable than constructors
    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount, Currency.getInstance(currencyCode));
    }

    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public static Money zero(String currencyCode) {
        return new Money(BigDecimal.ZERO, Currency.getInstance(currencyCode));
    }

    // Operations — always return a NEW Money, never modify this one
    public Money add(Money other) {
        assertSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(int quantity) {
        if (quantity < 0) throw new DomainException("Quantity cannot be negative");
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        assertSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    private void assertSameCurrency(Money other) {
        if (!this.currency.equals(other.currency))
            throw new DomainException("Currency mismatch: " + this.currency + " vs " + other.currency);
    }

    // Accessors — no setters, ever
    public BigDecimal amount() {
        return amount;
    }

    public Currency currency() {
        return currency;
    }

    // equals and hashCode based on VALUE, not reference
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money m)) return false;
        return amount.compareTo(m.amount) == 0 && currency.equals(m.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency.getCurrencyCode();
    }
}
