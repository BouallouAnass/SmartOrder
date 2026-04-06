package com.smartorder.infrastructure.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_lines")
public class OrderLineEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal unitPriceAmount;

    @Column(name = "unit_price_currency", length = 3, nullable = false)
    private String unitPriceCurrency;

    protected OrderLineEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public OrderEntity getOrder() { return order; }
    public void setOrder(OrderEntity order) { this.order = order; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPriceAmount() { return unitPriceAmount; }
    public void setUnitPriceAmount(BigDecimal unitPriceAmount) { this.unitPriceAmount = unitPriceAmount; }
    public String getUnitPriceCurrency() { return unitPriceCurrency; }
    public void setUnitPriceCurrency(String unitPriceCurrency) { this.unitPriceCurrency = unitPriceCurrency; }
}
