package com.smartorder.application.usecase;

import com.smartorder.domain.model.DomainException;
import com.smartorder.domain.model.Order;
import com.smartorder.domain.model.Product;
import com.smartorder.domain.port.OrderRepository;
import com.smartorder.domain.port.PlaceOrderUseCase;
import com.smartorder.domain.port.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlaceOrderUseCaseImpl implements PlaceOrderUseCase {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    public PlaceOrderUseCaseImpl(ProductRepository productRepository,
                                 OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public PlaceOrderResult execute(PlaceOrderCommand command) {

        Map<Product, Integer> products = getProductsFromItemList(command.items());

        Order order = Order.create(command.customerId());
        products.forEach(order::addLine);
        order.place();
        orderRepository.save(order);

        products.forEach((product, quantity) -> {
            product.adjustStock(-quantity);
            productRepository.save(product);
        });

        return new PlaceOrderResult(order.orderId(), order.status(), order.calculateTotal());
    }

    private Map<Product, Integer> getProductsFromItemList(List<OrderItemRequest> orderItemRequests) {
        return orderItemRequests.stream()
                .map(req -> {
                    Product product = productRepository.findById(req.productId())
                            .orElseThrow(() -> new DomainException("Product not found: " + req.productId()));
                    return Map.entry(product, req.quantity());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> {
                            throw new DomainException("Duplicate products in the same Order are not allowed");
                        }
                ));
    }
}
