package com.smartorder.domain.model;

import java.util.UUID;

public final class OrderId {

    private final UUID value;

    private OrderId(UUID value) {
        if (value == null) throw new IllegalArgumentException("OrderId cannot be null");
        this.value = value;
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderId id)) return false;
        return value.equals(id.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "OrderId(" + value + ")";
    }
}