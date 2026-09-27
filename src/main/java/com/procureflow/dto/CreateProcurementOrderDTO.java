package com.procureflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateProcurementOrderDTO {

    @NotNull(message = "Vendor id is required")
    private Long vendorId;

    @NotNull(message = "Order amount is required")
    @Positive(message = "Order amount must be greater than 0")
    private BigDecimal orderAmount;

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
}
