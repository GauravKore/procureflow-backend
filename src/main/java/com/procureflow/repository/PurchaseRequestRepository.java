package com.procureflow.repository;

import com.procureflow.entity.PurchaseRequest;
import com.procureflow.entity.RequestStatus;
import com.procureflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {

    List<PurchaseRequest> findByRequester(User requester);

    List<PurchaseRequest> findByStatus(RequestStatus status);
}
