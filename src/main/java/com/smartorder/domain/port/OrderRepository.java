package com.smartorder.domain.port;

import com.smartorder.domain.model.Order;
import com.smartorder.domain.model.OrderId;

import java.util.Optional;

/**
 * Outbound port — the domain's contract for persistence.
 * The domain defines this interface. Infrastructure implements it.
 *
 * This is the key inversion of hexagonal architecture:
 * the domain does NOT depend on JPA or any DB tech.
 * JPA adapter in infrastructure/ implements this port.
 */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId orderId);
}
