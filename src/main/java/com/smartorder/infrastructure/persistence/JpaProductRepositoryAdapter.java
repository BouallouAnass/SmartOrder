package com.smartorder.infrastructure.persistence;

import com.smartorder.domain.model.*;
import com.smartorder.domain.port.ProductRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpa;

    public JpaProductRepositoryAdapter(JpaProductRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return jpa.findById(productId.value())
                .map(this::toDomain);
    }

    @Override
    public List<Product> findAllActive() {
        return jpa.findByActiveTrue().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Product save(Product product) {
        ProductEntity saved = jpa.save(toEntity(product));
        return toDomain(saved);
    }

    private ProductEntity toEntity(Product product) {
        ProductEntity e = new ProductEntity();
        e.setId(product.getId().value());
        e.setName(product.getName());
        e.setDescription(product.getDescription());
        e.setPriceAmount(product.getPrice().amount());
        e.setPriceCurrency(product.getPrice().currency().getCurrencyCode());
        e.setStockQuantity(product.getStockQuantity());
        e.setActive(product.isActive());
        return e;
    }

    private Product toDomain(ProductEntity e) {
        return Product.reconstitute(
                ProductId.of(e.getId()),
                e.getName(),
                e.getDescription(),
                Money.of(e.getPriceAmount(), e.getPriceCurrency()),
                e.getStockQuantity(),
                e.getActive()
        );
    }
}
