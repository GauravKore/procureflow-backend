package com.procureflow.dto;

import com.procureflow.entity.Notification;

import java.time.LocalDateTime;

public class NotificationResponseDTO {

    private Long id;
    private String message;
    private Long purchaseRequestId;
    private boolean read;
    private LocalDateTime createdAt;

    public static NotificationResponseDTO fromEntity(Notification n) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.id = n.getId();
        dto.message = n.getMessage();
        dto.purchaseRequestId = n.getPurchaseRequestId();
        dto.read = n.isRead();
        dto.createdAt = n.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public String getMessage() { return message; }
    public Long getPurchaseRequestId() { return purchaseRequestId; }
    public boolean isRead() { return read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
