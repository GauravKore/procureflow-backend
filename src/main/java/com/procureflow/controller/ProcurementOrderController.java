package com.procureflow.controller;

import com.procureflow.dto.CreateProcurementOrderDTO;
import com.procureflow.dto.ProcurementOrderResponseDTO;
import com.procureflow.entity.ProcurementOrder;
import com.procureflow.service.ProcurementOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procurement-orders")
public class ProcurementOrderController {

    private final ProcurementOrderService procurementOrderService;

    public ProcurementOrderController(ProcurementOrderService procurementOrderService) {
        this.procurementOrderService = procurementOrderService;
    }

    /**
     * PROCUREMENT reviews a manager-approved purchase request, assigns a vendor and an
     * order amount, and forwards it to FINANCE for financial approval.
     */
    @PostMapping("/purchase-requests/{purchaseRequestId}")
    @PreAuthorize("hasRole('PROCUREMENT')")
    public ResponseEntity<ProcurementOrderResponseDTO> createOrder(
            @PathVariable Long purchaseRequestId,
            @Valid @RequestBody CreateProcurementOrderDTO dto,
            @RequestParam(required = false) String comment) {

        ProcurementOrder order = procurementOrderService.createOrder(purchaseRequestId, dto, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProcurementOrderResponseDTO.fromEntity(order));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PROCUREMENT','FINANCE','MANAGER')")
    public ResponseEntity<List<ProcurementOrderResponseDTO>> getAll() {
        List<ProcurementOrderResponseDTO> result = procurementOrderService.getAll()
                .stream().map(ProcurementOrderResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROCUREMENT','FINANCE','MANAGER')")
    public ResponseEntity<ProcurementOrderResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ProcurementOrderResponseDTO.fromEntity(procurementOrderService.getById(id)));
    }
}
