package com.procureflow.controller;

import com.procureflow.dto.DecisionDTO;
import com.procureflow.dto.PurchaseRequestCreateDTO;
import com.procureflow.dto.PurchaseRequestResponseDTO;
import com.procureflow.entity.PurchaseRequest;
import com.procureflow.entity.RequestStatus;
import com.procureflow.service.PurchaseRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-requests")
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    private String primaryRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("")
                .replace("ROLE_", "");
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PurchaseRequestResponseDTO> create(
            @Valid @RequestBody PurchaseRequestCreateDTO dto,
            Authentication authentication) {

        PurchaseRequest pr = purchaseRequestService.create(authentication.getName(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(PurchaseRequestResponseDTO.fromEntity(pr));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<PurchaseRequestResponseDTO>> myRequests(Authentication authentication) {
        List<PurchaseRequestResponseDTO> result = purchaseRequestService.getMyRequests(authentication.getName())
                .stream().map(PurchaseRequestResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','PROCUREMENT','FINANCE')")
    public ResponseEntity<List<PurchaseRequestResponseDTO>> getAll(
            @RequestParam(required = false) RequestStatus status) {

        List<PurchaseRequest> requests = status != null
                ? purchaseRequestService.getByStatus(status)
                : purchaseRequestService.getAll();

        List<PurchaseRequestResponseDTO> result = requests.stream()
                .map(PurchaseRequestResponseDTO::fromEntity).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequestResponseDTO> getById(
            @PathVariable Long id, Authentication authentication) {

        PurchaseRequest pr = purchaseRequestService.getForViewer(id, authentication.getName(), primaryRole(authentication));
        return ResponseEntity.ok(PurchaseRequestResponseDTO.fromEntity(pr));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<PurchaseRequestResponseDTO> approve(
            @PathVariable Long id, @RequestBody(required = false) DecisionDTO body) {

        String comment = body != null ? body.getComment() : null;
        PurchaseRequest pr = purchaseRequestService.managerDecision(id, true, comment);
        return ResponseEntity.ok(PurchaseRequestResponseDTO.fromEntity(pr));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<PurchaseRequestResponseDTO> reject(
            @PathVariable Long id, @RequestBody(required = false) DecisionDTO body) {

        String comment = body != null ? body.getComment() : null;
        PurchaseRequest pr = purchaseRequestService.managerDecision(id, false, comment);
        return ResponseEntity.ok(PurchaseRequestResponseDTO.fromEntity(pr));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PurchaseRequestResponseDTO> cancel(
            @PathVariable Long id, Authentication authentication) {

        PurchaseRequest pr = purchaseRequestService.cancel(id, authentication.getName());
        return ResponseEntity.ok(PurchaseRequestResponseDTO.fromEntity(pr));
    }
}
