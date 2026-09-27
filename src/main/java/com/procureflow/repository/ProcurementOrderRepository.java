package com.procureflow.repository;

import com.procureflow.entity.ProcurementOrder;
import com.procureflow.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcurementOrderRepository extends JpaRepository<ProcurementOrder, Long> {

    Optional<ProcurementOrder> findByPurchaseRequest(PurchaseRequest purchaseRequest);
}
