package com.procureflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_request_items")
public class PurchaseRequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_request_id", nullable = false)
    @JsonIgnore
    private PurchaseRequest purchaseRequest;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "estimated_unit_price", nullable = false)
    private BigDecimal estimatedUnitPrice;

    public BigDecimal getSubtotal() {
        if (quantity == null || estimatedUnitPrice == null) return BigDecimal.ZERO;
        return estimatedUnitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() { return id; }

    public PurchaseRequest getPurchaseRequest() { return purchaseRequest; }
    public void setPurchaseRequest(PurchaseRequest purchaseRequest) { this.purchaseRequest = purchaseRequest; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getEstimatedUnitPrice() { return estimatedUnitPrice; }
    public void setEstimatedUnitPrice(BigDecimal estimatedUnitPrice) { this.estimatedUnitPrice = estimatedUnitPrice; }
}
