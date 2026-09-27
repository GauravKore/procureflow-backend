package com.procureflow.service;

import com.procureflow.dto.CreateProcurementOrderDTO;
import com.procureflow.entity.ProcurementOrder;
import com.procureflow.entity.PurchaseRequest;
import com.procureflow.entity.Vendor;
import com.procureflow.exception.ResourceNotFoundException;
import com.procureflow.repository.ProcurementOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcurementOrderService {

    private final ProcurementOrderRepository procurementOrderRepository;
    private final PurchaseRequestService purchaseRequestService;
    private final VendorService vendorService;

    public ProcurementOrderService(ProcurementOrderRepository procurementOrderRepository,
                                    PurchaseRequestService purchaseRequestService,
                                    VendorService vendorService) {
        this.procurementOrderRepository = procurementOrderRepository;
        this.purchaseRequestService = purchaseRequestService;
        this.vendorService = vendorService;
    }

    /**
     * PROCUREMENT reviews a manager-approved request, assigns a vendor,
     * creates the order, and forwards it to FINANCE.
     */
    @Transactional
    public ProcurementOrder createOrder(Long purchaseRequestId, CreateProcurementOrderDTO dto, String comment) {
        Vendor vendor = vendorService.getById(dto.getVendorId());

        PurchaseRequest pr = purchaseRequestService.attachVendorAndComment(purchaseRequestId, vendor, comment);

        ProcurementOrder order = new ProcurementOrder();
        order.setPurchaseRequest(pr);
        order.setVendor(vendor);
        order.setOrderAmount(dto.getOrderAmount());

        ProcurementOrder saved = procurementOrderRepository.save(order);

        purchaseRequestService.markPendingFinanceApproval(pr);

        return saved;
    }

    public ProcurementOrder getById(Long id) {
        return procurementOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procurement order not found: " + id));
    }

    public ProcurementOrder getByPurchaseRequest(PurchaseRequest pr) {
        return procurementOrderRepository.findByPurchaseRequest(pr)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No procurement order exists for purchase request " + pr.getId()));
    }

    public List<ProcurementOrder> getAll() {
        return procurementOrderRepository.findAll();
    }
}
