package com.smartorder.domain.port;

import com.smartorder.domain.model.Product;
import com.smartorder.domain.model.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port — the domain's contract for product persistence.
 *
 * Notice: returns domain objects (Product), not JPA entities.
 * The JPA adapter in infrastructure/ maps between the two.
 */
public interface ProductRepository {

    Optional<Product> findById(ProductId productId);

    List<Product> findAllActive();

    Product save(Product product);
}
