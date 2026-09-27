package com.procureflow.dto;

import com.procureflow.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponseDTO {

    private Long id;
    private Long procurementOrderId;
    private String vendorName;
    private BigDecimal amount;
    private String status;
    private String financeComment;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;

    public static PaymentResponseDTO fromEntity(Payment p) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.id = p.getId();
        dto.procurementOrderId = p.getProcurementOrder().getId();
        dto.vendorName = p.getProcurementOrder().getVendor().getName();
        dto.amount = p.getAmount();
        dto.status = p.getStatus().name();
        dto.financeComment = p.getFinanceComment();
        dto.paidAt = p.getPaidAt();
        dto.createdAt = p.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public Long getProcurementOrderId() { return procurementOrderId; }
    public String getVendorName() { return vendorName; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getFinanceComment() { return financeComment; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
