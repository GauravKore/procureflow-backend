package com.procureflow.service;

import com.procureflow.entity.Payment;
import com.procureflow.entity.PaymentStatus;
import com.procureflow.entity.ProcurementOrder;
import com.procureflow.entity.PurchaseRequest;
import com.procureflow.exception.InvalidStatusTransitionException;
import com.procureflow.exception.ResourceNotFoundException;
import com.procureflow.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProcurementOrderService procurementOrderService;
    private final PurchaseRequestService purchaseRequestService;

    public PaymentService(PaymentRepository paymentRepository,
                           ProcurementOrderService procurementOrderService,
                           PurchaseRequestService purchaseRequestService) {
        this.paymentRepository = paymentRepository;
        this.procurementOrderService = procurementOrderService;
        this.purchaseRequestService = purchaseRequestService;
    }

    /** FINANCE approves or rejects the financial side of an already-created procurement order. */
    @Transactional
    public Payment decide(Long purchaseRequestId, boolean approve, String comment) {
        PurchaseRequest pr = purchaseRequestService.financeDecision(purchaseRequestId, approve, comment);
        ProcurementOrder order = procurementOrderService.getByPurchaseRequest(pr);

        Payment payment = paymentRepository.findByProcurementOrder(order).orElseGet(Payment::new);
        payment.setProcurementOrder(order);
        payment.setAmount(order.getOrderAmount());
        payment.setFinanceComment(comment);
        payment.setStatus(approve ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);

        return paymentRepository.save(payment);
    }

    /** FINANCE marks an approved payment as actually paid, completing the procurement cycle. */
    @Transactional
    public Payment markPaid(Long paymentId) {
        Payment payment = getById(paymentId);

        if (payment.getStatus() != PaymentStatus.APPROVED) {
            throw new InvalidStatusTransitionException(
                    "Only an approved payment can be marked as paid (current status: " + payment.getStatus() + ")");
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);

        purchaseRequestService.markCompleted(payment.getProcurementOrder().getPurchaseRequest().getId());

        return saved;
    }

    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }
}
