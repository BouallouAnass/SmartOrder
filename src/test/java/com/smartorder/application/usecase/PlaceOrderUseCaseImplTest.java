package com.smartorder.application.usecase;

import com.smartorder.domain.model.DomainException;
import com.smartorder.domain.model.Money;
import com.smartorder.domain.model.Order;
import com.smartorder.domain.model.Product;
import com.smartorder.domain.port.OrderRepository;
import com.smartorder.domain.port.PlaceOrderUseCase;
import com.smartorder.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderUseCaseImplTest {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private PlaceOrderUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        useCase = new PlaceOrderUseCaseImpl(productRepository, orderRepository);
    }

    private Product aProduct() {
        return Product.create("Iphone", "A new phone", Money.of("800", "EUR"), 10);
    }

    private Product aProduct2() {
        return Product.create("Samsung", "A new phone", Money.of("700", "EUR"), 20);
    }

    @Test
    void should_place_order_successfully() {
        // ARRANGE — set up your product, command, and mock behaviour
        Product product1 = aProduct();
        Product product2 = aProduct2();

        List<PlaceOrderUseCase.OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(new PlaceOrderUseCase.OrderItemRequest(product1.getId(), 2));
        orderItemRequests.add(new PlaceOrderUseCase.OrderItemRequest(product2.getId(), 3));

        when(productRepository.findById(product1.getId())).thenReturn(Optional.of(product1));
        when(productRepository.findById(product2.getId())).thenReturn(Optional.of(product2));

        PlaceOrderUseCase.PlaceOrderCommand placeOrderCommand = new PlaceOrderUseCase.PlaceOrderCommand(UUID.randomUUID(), orderItemRequests);

        // ACT — call useCase.execute(command)
        PlaceOrderUseCase.PlaceOrderResult placeOrderResult = useCase.execute(placeOrderCommand);

        // ASSERT — check the result and verify interactions
        verify(orderRepository).save(any(Order.class));
        assertEquals(Order.Status.PLACED, placeOrderResult.status());
        assertEquals(Money.of("3700", "EUR"), placeOrderResult.orderTotal());
        verify(productRepository).save(product1);
        verify(productRepository).save(product2);
        assertEquals(8, product1.getStockQuantity());  // 10 - 2
        assertEquals(17, product2.getStockQuantity()); // 20 - 3
    }

    @Test
    void product_not_found_KO() {
        Product product1 = aProduct();

        List<PlaceOrderUseCase.OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(new PlaceOrderUseCase.OrderItemRequest(product1.getId(), 2));

        when(productRepository.findById(product1.getId())).thenReturn(Optional.ofNullable(null));

        PlaceOrderUseCase.PlaceOrderCommand placeOrderCommand = new PlaceOrderUseCase.PlaceOrderCommand(UUID.randomUUID(), orderItemRequests);

        assertThrows(DomainException.class, () -> useCase.execute(placeOrderCommand));
    }

    @Test
    void duplicate_product_KO() {
        Product product1 = aProduct();
        List<PlaceOrderUseCase.OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(new PlaceOrderUseCase.OrderItemRequest(product1.getId(), 2));
        orderItemRequests.add(new PlaceOrderUseCase.OrderItemRequest(product1.getId(), 3));

        when(productRepository.findById(product1.getId())).thenReturn(Optional.of(product1));

        PlaceOrderUseCase.PlaceOrderCommand placeOrderCommand = new PlaceOrderUseCase.PlaceOrderCommand(UUID.randomUUID(), orderItemRequests);
        assertThrows(DomainException.class, () -> useCase.execute(placeOrderCommand));
    }

    @Test
    void order_placement_fail_KO() {
        Product product1 = aProduct();
        List<PlaceOrderUseCase.OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(new PlaceOrderUseCase.OrderItemRequest(product1.getId(), 2));

        PlaceOrderUseCase.PlaceOrderCommand placeOrderCommand = new PlaceOrderUseCase.PlaceOrderCommand(UUID.randomUUID(), orderItemRequests);

        assertThrows(DomainException.class, () -> useCase.execute(placeOrderCommand));
    }
}
