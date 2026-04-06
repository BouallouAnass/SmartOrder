package com.smartorder.domain.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;


public class OrderTest {

    private final UUID customerId = UUID.randomUUID();

    private Product aProduct() {
        return Product.create("Iphone", "A new phone", Money.of("800", "EUR"), 10);
    }

    private Product aProductJPY() {
        return Product.create("Samsung Galaxy", "A new phone", Money.of("700", "JPY"), 20);
    }

    private OrderLine anOrderLine() {
        return OrderLine.of(aProduct(), 5);
    }

    private Order anOrder() {
        return Order.create(customerId);
    }


    @Test
    void test_from_draft_to_placed() {
        Order order = anOrder();
        order.addLine(aProduct(), 2);

        order.place();
        assertEquals(Order.Status.PLACED, order.status());
    }

    @Test
    void test_from_draft_to_cancel() {
        Order order = anOrder();
        order.cancel();
        assertEquals(Order.Status.CANCELLED, order.status());
    }

    @Test
    void test_from_placed_to_cancel() {
        Order order = anOrder();
        order.addLine(aProduct(), 2);

        order.place();
        order.cancel();
        assertEquals(Order.Status.CANCELLED, order.status());
    }

    @Test
    @Disabled("confirm() not yet implemented — revisit in week 2")
    void test_from_confirmed_to_cancel() {
        // placeholder
    }

    @Test
    void test_from_placed_to_placed() {
        Order order = anOrder();
        order.addLine(aProduct(), 2);

        order.place();
        assertThrows(DomainException.class, order::place);
    }

    @Test
    void test_addline_status_not_draft() {
        Order order = anOrder();
        order.addLine(aProduct(), 2);

        order.place();
        assertThrows(DomainException.class, () -> order.addLine(aProduct(), 2));
    }

    @Test
    void test_addline_quantity_zero() {
        Order order = anOrder();
        assertThrows(DomainException.class, () -> order.addLine(aProduct(), 0));
    }

    @Test
    void test_addline_product_unavailable() {
        Order order = anOrder();
        Product product = aProduct();
        product.deactivate();
        assertThrows(DomainException.class, () -> order.addLine(product, 2));
    }

    @Test
    void test_addline_currency_mismatch() {
        Order order = anOrder();
        order.addLine(aProduct(), 2);
        assertThrows(DomainException.class, () -> order.addLine(aProductJPY(), 2));

    }

    @Test
    void placed_order_cannot_have_lines_added() {
        // you have test_addline_status_not_draft which covers this
        // but you're missing the symmetric case:
        // cancelled order also cannot have lines added
        Order order = anOrder();
        order.cancel();
        assertThrows(DomainException.class, () -> order.addLine(aProduct(), 1));
    }

    @Test
    void test_addline_success() {
        Order order = anOrder();
        order.addLine(aProduct(), 2);
        List<OrderLine> lines = order.getOrderLines();
        assertEquals(1, lines.size());
        assertEquals("Iphone", lines.get(0).productName());
        assertEquals(2, lines.get(0).quantity());
    }

    @Test
    void test_total_empty_order() {
        Order order = anOrder();
        assertThrows(DomainException.class, order::calculateTotal);
    }

    @Test
    void test_total_one_line() {
        Order order = anOrder();
        order.addLine(aProduct(), 2);
        Money expected = OrderLine.of(aProduct(), 2).subtotal();

        assertEquals(expected, order.calculateTotal());
    }

    @Test
    void test_total_two_lines() {
        Order order = anOrder();
        order.addLine(aProduct(), 2);
        order.addLine(aProduct(), 5);

        Money expected = OrderLine.of(aProduct(), 2).subtotal().add(OrderLine.of(aProduct(), 5).subtotal());

        assertEquals(expected, order.calculateTotal());

    }

}
