package com.smartorder.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class Order {

    public enum Status {
        DRAFT, PLACED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    private final OrderId orderId;
    private final UUID customerId;
    private final List<OrderLine> orderLines;
    private Status status;
    private Instant placedAt;

    private Order(UUID customerId) {
        if (customerId == null) throw new DomainException("CustomerId cannot be null");
        this.orderId = OrderId.generate();
        this.customerId = customerId;
        this.status = Status.DRAFT;
        this.orderLines = new ArrayList<>();
    }

    private Order(UUID customerId, OrderId orderId, List<OrderLine> orderLines, Instant placedAt, Status status) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.orderLines = orderLines;
        this.placedAt = placedAt;
        this.status = status;
    }

    public static Order create(UUID customerId) {
        return new Order(customerId);
    }

    public static Order reconstitute(OrderId orderId, UUID customerId, List<OrderLine> orderLines, Status status, Instant placedAt) {
        return new Order(customerId, orderId, orderLines, placedAt, status);
    }

    public void addLine(Product product, int quantity) {
        if (this.status != Status.DRAFT)
            throw new DomainException("Cannot add lines to order in status: " + status);
        if (quantity <= 0)
            throw new DomainException("Quantity must be positive, got: " + quantity);
        if (!product.isAvailable(quantity))
            throw new DomainException("Product not available: " + product.getName());
        if (!orderLines.isEmpty() &&
                !product.getPrice().currency().equals(orderLines.get(0).unitPrice().currency()))
            throw new DomainException("All order lines must use the same currency");

        // No stock adjustment here — that's PlaceOrderUseCase's responsibility
        orderLines.add(OrderLine.of(product, quantity));
    }

    public void place() {
        if (this.status != Status.DRAFT)
            throw new DomainException("Only DRAFT orders can be placed, current: " + status);
        if (orderLines.isEmpty())
            throw new DomainException("Cannot place an empty order");

        this.status = Status.PLACED;
        this.placedAt = Instant.now();
    }

    public void cancel() {
        if (this.status != Status.DRAFT && this.status != Status.PLACED)
            throw new DomainException("Can only cancel DRAFT or PLACED orders, current: " + status);

        this.status = Status.CANCELLED;
    }

    public Money calculateTotal() {
        if (orderLines.isEmpty())
            throw new DomainException("Cannot calculate total of an empty order");
        return orderLines.stream()
                .map(OrderLine::subtotal)
                .reduce(Money::add)
                .orElseThrow();
    }

    public List<OrderLine> getOrderLines() {
        return List.copyOf(orderLines);
    }

    public OrderId orderId() {
        return orderId;
    }

    public UUID customerId() {
        return customerId;
    }

    public Status status() {
        return status;
    }

    public Instant placedAt() {
        return placedAt;
    }
}
