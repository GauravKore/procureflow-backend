package com.procureflow.repository;

import com.procureflow.entity.Payment;
import com.procureflow.entity.ProcurementOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByProcurementOrder(ProcurementOrder procurementOrder);
}
