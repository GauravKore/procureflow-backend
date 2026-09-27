package com.procureflow.controller;

import com.procureflow.dto.DecisionDTO;
import com.procureflow.dto.PaymentResponseDTO;
import com.procureflow.entity.Payment;
import com.procureflow.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /** FINANCE approves or rejects the financial side of a purchase request that has a procurement order. */
    @PostMapping("/purchase-requests/{purchaseRequestId}/approve")
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<PaymentResponseDTO> approve(
            @PathVariable Long purchaseRequestId, @RequestBody(required = false) DecisionDTO body) {

        String comment = body != null ? body.getComment() : null;
        Payment payment = paymentService.decide(purchaseRequestId, true, comment);
        return ResponseEntity.ok(PaymentResponseDTO.fromEntity(payment));
    }

    @PostMapping("/purchase-requests/{purchaseRequestId}/reject")
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<PaymentResponseDTO> reject(
            @PathVariable Long purchaseRequestId, @RequestBody(required = false) DecisionDTO body) {

        String comment = body != null ? body.getComment() : null;
        Payment payment = paymentService.decide(purchaseRequestId, false, comment);
        return ResponseEntity.ok(PaymentResponseDTO.fromEntity(payment));
    }

    @PostMapping("/{paymentId}/mark-paid")
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<PaymentResponseDTO> markPaid(@PathVariable Long paymentId) {
        Payment payment = paymentService.markPaid(paymentId);
        return ResponseEntity.ok(PaymentResponseDTO.fromEntity(payment));
    }

    @GetMapping
    @PreAuthorize("hasRole('FINANCE')")
    public ResponseEntity<List<PaymentResponseDTO>> getAll() {
        List<PaymentResponseDTO> result = paymentService.getAll()
                .stream().map(PaymentResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(result);
    }
}
