package com.smartorder.domain.model;

import java.util.UUID;

/**
 * YOUR TASK — belongs to the Order aggregate. Never standalone.
 * <p>
 * Fields to include:
 * - OrderLineId (or just UUID — your call, justify it in your journal)
 * - ProductId productId
 * - String productName (snapshot — price/name can change, order is a record)
 * - int quantity
 * - Money unitPrice (snapshot at time of ordering)
 * <p>
 * Methods:
 * - static OrderLine of(Product product, int quantity) — factory
 * - Money subtotal() — unitPrice × quantity
 * <p>
 * LEARNING NOTE — why snapshot productName and unitPrice?
 * A product's price might change tomorrow.
 * An order placed today must always show what the customer paid.
 * This is the "snapshot pattern" — fundamental in order management systems.
 * You'll mention this in interviews and it will land well.
 */
public class OrderLine {

    // YOUR CODE HERE
    private UUID id;

    private ProductId productId;

    private String productName;

    private int quantity;

    private Money unitPrice;


    private OrderLine(Product product, int quantity) {
        if (quantity <= 0)
            throw new DomainException("Quantity must be positive, got: " + quantity);
        this.id = UUID.randomUUID();
        this.productId = product.getId();
        this.productName = product.getName();
        this.unitPrice = product.getPrice();
        this.quantity = quantity;
    }

    private OrderLine(UUID id, ProductId productId, String productName, int quantity, Money unitPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderLine of(Product product, int quantity) {
        return new OrderLine(product, quantity);
    }

    public static OrderLine reconstitute(UUID id, ProductId productId, String productName, int quantity, Money unitPrice) {
        return new OrderLine(id, productId, productName, quantity, unitPrice);
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    public UUID id() {
        return id;
    }

    public ProductId productId() {
        return productId;
    }

    public String productName() {
        return productName;
    }

    public int quantity() {
        return quantity;
    }

    public Money unitPrice() {
        return unitPrice;
    }
}
