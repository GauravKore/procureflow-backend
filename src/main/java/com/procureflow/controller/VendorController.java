package com.procureflow.controller;

import com.procureflow.dto.VendorDTO;
import com.procureflow.entity.Vendor;
import com.procureflow.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('PROCUREMENT')")
    public ResponseEntity<VendorDTO> create(@Valid @RequestBody VendorDTO dto) {
        Vendor vendor = vendorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(VendorDTO.fromEntity(vendor));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PROCUREMENT','MANAGER','FINANCE')")
    public ResponseEntity<List<VendorDTO>> getAll() {
        List<VendorDTO> result = vendorService.getAll().stream().map(VendorDTO::fromEntity).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROCUREMENT','MANAGER','FINANCE')")
    public ResponseEntity<VendorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(VendorDTO.fromEntity(vendorService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROCUREMENT')")
    public ResponseEntity<VendorDTO> update(@PathVariable Long id, @Valid @RequestBody VendorDTO dto) {
        return ResponseEntity.ok(VendorDTO.fromEntity(vendorService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROCUREMENT')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        vendorService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
