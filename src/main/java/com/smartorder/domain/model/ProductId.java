package com.smartorder.domain.model;

import java.util.UUID;

/**
 * YOUR TASK — same pattern as OrderId.
 * <p>
 * Once you've done OrderId, ProductId should take you 3 minutes.
 * That's the point: consistent patterns reduce cognitive load.
 */
public final class ProductId {

    private final UUID value;


    private ProductId(UUID value) {
        if (value == null) throw new IllegalArgumentException("ProductId cannot be null");
        this.value = value;
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductId id)) return false;
        return this.value.equals(id.value());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "ProductId (" + value + ")";
    }

}
