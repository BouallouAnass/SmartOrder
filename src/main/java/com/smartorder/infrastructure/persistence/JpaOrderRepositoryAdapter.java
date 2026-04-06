package com.smartorder.infrastructure.persistence;

import com.smartorder.domain.model.*;
import com.smartorder.domain.port.OrderRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound adapter — implements the domain port using JPA.
 * This is where domain objects get mapped to/from JPA entities.
 * <p>
 * The domain owns the port interface (OrderRepository).
 * Infrastructure owns this adapter.
 * Spring wires them together via @Component + @Autowired.
 * <p>
 * NOTE: toDomain() and toEntity() are the mapping methods.
 * They will need completing once you implement your domain classes.
 * The TODOs below mark exactly what you need to fill in as you build
 * out Order, OrderLine, and Money.
 */
@Component
public class JpaOrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpa;

    public JpaOrderRepositoryAdapter(JpaOrderRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return jpa.findById(orderId.value())
                .map(this::toDomain);
    }

    // --- Mapping: domain → entity ---

    private OrderEntity toEntity(Order order) {
        OrderEntity e = new OrderEntity();
        // TODO: fill these in as you implement your Order aggregate fields
        e.setId(order.orderId().value());
        e.setCustomerId(order.customerId());
        e.setStatus(order.status().name());
        e.setPlacedAt(order.placedAt());
        e.setCreatedAt(Instant.now());
        e.setUpdatedAt(Instant.now());
        // for each line: create OrderLineEntity, set order reference, add to list
        e.setLines(order.getOrderLines().stream().map(orderLine -> {
            OrderLineEntity orderLineEntity = new OrderLineEntity();
            orderLineEntity.setOrder(e);
            orderLineEntity.setId(orderLine.id());
            orderLineEntity.setProductId(orderLine.productId().value());
            orderLineEntity.setQuantity(orderLine.quantity());
            orderLineEntity.setProductName(orderLine.productName());
            orderLineEntity.setUnitPriceCurrency(orderLine.unitPrice().currency().getCurrencyCode());
            orderLineEntity.setUnitPriceAmount(orderLine.unitPrice().amount());
            return orderLineEntity;
        }).toList());
        return e;
    }

    // --- Mapping: entity → domain ---

    private Order toDomain(OrderEntity e) {
        // TODO: reconstruct your Order aggregate from the entity
        // This is called "rehydration" — rebuilding domain state from persistence
        // You'll need a package-private or static factory on Order for this
        return Order.reconstitute(OrderId.of(e.getId()),
                e.getCustomerId(),
                e.getLines().stream().map(orderLineEntity -> {
                    return OrderLine.reconstitute(orderLineEntity.getId(),
                            ProductId.of(orderLineEntity.getProductId()),
                            orderLineEntity.getProductName(),
                            orderLineEntity.getQuantity(),
                            Money.of(orderLineEntity.getUnitPriceAmount(), orderLineEntity.getUnitPriceCurrency()));
                }).toList(),
                Order.Status.valueOf(e.getStatus()),
                e.getPlacedAt());
    }
}
