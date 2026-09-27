package com.procureflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class PurchaseRequestItemDTO {

    private Long id;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Estimated unit price is required")
    @Positive(message = "Estimated unit price must be greater than 0")
    private BigDecimal estimatedUnitPrice;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getEstimatedUnitPrice() { return estimatedUnitPrice; }
    public void setEstimatedUnitPrice(BigDecimal estimatedUnitPrice) { this.estimatedUnitPrice = estimatedUnitPrice; }
}
