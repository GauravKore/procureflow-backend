package com.procureflow.dto;

import com.procureflow.entity.ProcurementOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProcurementOrderResponseDTO {

    private Long id;
    private Long purchaseRequestId;
    private String purchaseRequestTitle;
    private String vendorName;
    private BigDecimal orderAmount;
    private LocalDateTime createdAt;

    public static ProcurementOrderResponseDTO fromEntity(ProcurementOrder o) {
        ProcurementOrderResponseDTO dto = new ProcurementOrderResponseDTO();
        dto.id = o.getId();
        dto.purchaseRequestId = o.getPurchaseRequest().getId();
        dto.purchaseRequestTitle = o.getPurchaseRequest().getTitle();
        dto.vendorName = o.getVendor().getName();
        dto.orderAmount = o.getOrderAmount();
        dto.createdAt = o.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public Long getPurchaseRequestId() { return purchaseRequestId; }
    public String getPurchaseRequestTitle() { return purchaseRequestTitle; }
    public String getVendorName() { return vendorName; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
