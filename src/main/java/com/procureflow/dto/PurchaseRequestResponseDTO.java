package com.procureflow.dto;

import com.procureflow.entity.PurchaseRequest;
import com.procureflow.entity.PurchaseRequestItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseRequestResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String requesterName;
    private String requesterEmail;
    private String managerComment;
    private String procurementComment;
    private String financeComment;
    private String vendorName;
    private BigDecimal estimatedTotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PurchaseRequestItemDTO> items;

    public static PurchaseRequestResponseDTO fromEntity(PurchaseRequest pr) {
        PurchaseRequestResponseDTO dto = new PurchaseRequestResponseDTO();
        dto.id = pr.getId();
        dto.title = pr.getTitle();
        dto.description = pr.getDescription();
        dto.status = pr.getStatus().name();
        dto.requesterName = pr.getRequester().getName();
        dto.requesterEmail = pr.getRequester().getEmail();
        dto.managerComment = pr.getManagerComment();
        dto.procurementComment = pr.getProcurementComment();
        dto.financeComment = pr.getFinanceComment();
        dto.vendorName = pr.getVendor() != null ? pr.getVendor().getName() : null;
        dto.estimatedTotal = pr.getEstimatedTotal();
        dto.createdAt = pr.getCreatedAt();
        dto.updatedAt = pr.getUpdatedAt();
        dto.items = pr.getItems().stream().map(PurchaseRequestResponseDTO::toItemDTO).toList();
        return dto;
    }

    private static PurchaseRequestItemDTO toItemDTO(PurchaseRequestItem item) {
        PurchaseRequestItemDTO d = new PurchaseRequestItemDTO();
        d.setId(item.getId());
        d.setItemName(item.getItemName());
        d.setQuantity(item.getQuantity());
        d.setEstimatedUnitPrice(item.getEstimatedUnitPrice());
        return d;
    }

    // Getters (no setters needed - read only response)

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getRequesterName() { return requesterName; }
    public String getRequesterEmail() { return requesterEmail; }
    public String getManagerComment() { return managerComment; }
    public String getProcurementComment() { return procurementComment; }
    public String getFinanceComment() { return financeComment; }
    public String getVendorName() { return vendorName; }
    public BigDecimal getEstimatedTotal() { return estimatedTotal; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<PurchaseRequestItemDTO> getItems() { return items; }
}
