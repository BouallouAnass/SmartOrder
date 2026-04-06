package com.smartorder.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product aProduct() {
        return Product.create("Iphone", "A new phone", Money.of("800", "EUR"), 10);
    }

    @Test
    void create_product_with_valid_data_succeeds() {
        Product product = aProduct();
        assertEquals("Iphone", product.getName());
        assertEquals(Money.of("800", "EUR"), product.getPrice());
        assertEquals(10, product.getStockQuantity());
        assertTrue(product.isActive());
    }

    @Test
    void cannot_create_product_with_blank_name() {
        assertThrows(DomainException.class, () ->
                Product.create("", "desc", Money.of("800", "EUR"), 10));
    }

    @Test
    void cannot_create_product_with_negative_stock() {
        assertThrows(DomainException.class, () ->
                Product.create("Iphone", "desc", Money.of("800", "EUR"), -10));
    }

    @Test
    void is_available_returns_true_when_stock_is_sufficient() {
        assertTrue(aProduct().isAvailable(10)); // exact boundary
    }

    @Test
    void is_available_returns_false_when_insufficient_stock() {
        assertFalse(aProduct().isAvailable(11)); // one over boundary
    }

    @Test
    void deactivate_makes_product_unavailable_regardless_of_stock() {
        Product product = aProduct();
        product.deactivate();
        assertFalse(product.isActive());
        assertFalse(product.isAvailable(1));
    }

    @Test
    void adjust_stock_increases_and_decreases_correctly() {
        Product product = aProduct();
        product.adjustStock(5);
        assertEquals(15, product.getStockQuantity());
        product.adjustStock(-10);
        assertEquals(5, product.getStockQuantity());
    }

    @Test
    void adjust_stock_throws_when_result_would_be_negative() {
        assertThrows(DomainException.class, () -> aProduct().adjustStock(-20));
    }
}
