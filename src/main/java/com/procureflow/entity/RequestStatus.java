package com.procureflow.entity;

public enum RequestStatus {
    PENDING_MANAGER_APPROVAL,
    REJECTED_BY_MANAGER,
    APPROVED_BY_MANAGER,      // now with Procurement
    VENDOR_ASSIGNED,          // Procurement has created the order
    PENDING_FINANCE_APPROVAL,
    REJECTED_BY_FINANCE,
    APPROVED_BY_FINANCE,
    COMPLETED,
    CANCELLED
}
