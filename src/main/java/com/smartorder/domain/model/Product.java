package com.smartorder.domain.model;

public class Product {

    private final ProductId id;
    private String name;
    private String description;
    private Money price;
    private int stockQuantity;
    private boolean active;

    // Private constructor — use the factory method
    private Product(ProductId id, String name, String description,
                    Money price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.active = true;
    }

    // Private constructor — use the factory method
    private Product(ProductId id, String name, String description,
                    Money price, int stockQuantity, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.active = active;
    }

    // Factory method — validates before creating
    public static Product create(String name, String description,
                                 Money price, int initialStock) {
        if (name == null || name.isBlank())
            throw new DomainException("Product name cannot be blank");
        if (initialStock < 0)
            throw new DomainException("Initial stock cannot be negative");

        return new Product(ProductId.generate(), name, description, price, initialStock);
    }

    public static Product reconstitute(ProductId id, String name, String description,
                                       Money price, int stock, boolean active) {
        return new Product(id, name, description, price, stock, active);
    }

    // Business methods — not setters, meaningful operations
    public boolean isAvailable(int requestedQuantity) {
        return active && stockQuantity >= requestedQuantity;
    }

    public void deactivate() {
        this.active = false;
    }

    public void adjustStock(int delta) {
        int newStock = this.stockQuantity + delta;
        if (newStock < 0)
            throw new DomainException(
                    "Insufficient stock. Available: " + stockQuantity + ", requested reduction: " + Math.abs(delta));
        this.stockQuantity = newStock;
    }

    // Read-only accessors
    public ProductId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public boolean isActive() {
        return active;
    }
}
