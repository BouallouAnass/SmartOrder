package com.smartorder.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void two_money_with_same_value_are_equal() {
        Money money1 = Money.of("50", "EUR");
        Money money2 = Money.of(new BigDecimal("50"), "EUR");
        assertEquals(money1, money2);
    }

    @Test
    void add_returns_new_money_with_correct_sum() {
        Money result = Money.of("50", "EUR").add(Money.of("50", "EUR"));
        assertEquals(Money.of("100", "EUR"), result);
    }

    @Test
    void add_is_commutative() {
        Money a = Money.of("30", "EUR");
        Money b = Money.of("20", "EUR");
        assertEquals(a.add(b), b.add(a));
    }

    @Test
    void add_throws_when_currencies_differ() {
        Money eur = Money.of("50", "EUR");
        Money jpy = Money.of("50", "JPY");
        assertThrows(DomainException.class, () -> eur.add(jpy));
    }

    @Test
    void multiply_returns_correct_subtotal() {
        Money result = Money.of("50", "EUR").multiply(2);
        assertEquals(Money.of("100", "EUR"), result);
    }

    @Test
    void cannot_create_money_with_negative_amount() {
        assertThrows(DomainException.class, () -> Money.of("-1", "EUR"));
    }

    @Test
    void zero_factory_creates_zero_money() {
        assertEquals(0, Money.zero("EUR").amount().compareTo(BigDecimal.ZERO));
    }
}