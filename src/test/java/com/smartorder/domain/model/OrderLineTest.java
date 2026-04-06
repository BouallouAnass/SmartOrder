package com.smartorder.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderLineTest {

    private Product aProduct() {
        return Product.create("Iphone", "A new phone", Money.of("800", "EUR"), 10);
    }

    private OrderLine anOrderLine() {
        return OrderLine.of(aProduct(), 5);
    }

    @Test
    void quantity_should_be_greater_than_zero() {
        assertThrows(DomainException.class, () -> OrderLine.of(aProduct(), -1));
    }

    @Test
    void quantity_zero_is_rejected() {
        assertThrows(DomainException.class, () -> OrderLine.of(aProduct(), 0));
    }

    @Test
    void create_orderline_with_valid_data() {
        OrderLine orderLine = anOrderLine();
        assertEquals("Iphone", orderLine.productName());
        assertEquals(5, orderLine.quantity());
        assertEquals(orderLine.unitPrice(), Money.of("800", "EUR"));
    }

    @Test
    void subtotal_return_adequate_value() {
        OrderLine orderLine = anOrderLine();
        assertEquals(orderLine.subtotal(), Money.of("4000", "EUR"));
    }
}
