package com.smartorder.domain.port;

import com.smartorder.domain.model.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Inbound port — defines what the application CAN DO from the outside world's perspective.
 * <p>
 * YOUR TASK (Week 1, Day 3-4):
 * Create PlaceOrderCommand as a record in this package.
 * Create PlaceOrderResult as a record in this package.
 * Implement this interface in application/usecase/PlaceOrderUseCaseImpl.java
 * <p>
 * The implementation must:
 * - Validate the command
 * - Load each product via the ProductRepository port
 * - Build an Order aggregate with OrderLines
 * - Call order.place() to enforce business rules
 * - Persist via OrderRepository port
 * - Return a PlaceOrderResult
 */
public interface PlaceOrderUseCase {

    PlaceOrderResult execute(PlaceOrderCommand command);

    // TODO: define PlaceOrderCommand record — what data does placing an order need?
    // Think: who is placng it? what products? what quantities?
    record PlaceOrderCommand(UUID customerId, List<OrderItemRequest> items) {
        public PlaceOrderCommand {
            Objects.requireNonNull(customerId, "customerId must not be null");
            Objects.requireNonNull(items, "items must not be null");
            if (items.isEmpty()) throw new IllegalArgumentException("Order must have at least one item");
            items = List.copyOf(items); // defensive copy
        }
    }

    // TODO: define PlaceOrderResult record — what do we return after placing?
    record PlaceOrderResult(
            // YOU DEFINE THIS — orderId? status? total?
            OrderId orderId,
            Order.Status status,
            Money orderTotal
    ) {
    }

    record OrderItemRequest(ProductId productId, int quantity) {
        public OrderItemRequest {
            Objects.requireNonNull(productId, "productId must not be null");
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
