package com.procureflow.service;

import com.procureflow.dto.VendorDTO;
import com.procureflow.entity.Vendor;
import com.procureflow.exception.DuplicateResourceException;
import com.procureflow.exception.ResourceNotFoundException;
import com.procureflow.repository.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public Vendor create(VendorDTO dto) {
        boolean exists = vendorRepository.findAll().stream()
                .anyMatch(v -> v.getEmail().equalsIgnoreCase(dto.getEmail()));

        if (exists) {
            throw new DuplicateResourceException("A vendor with this email already exists");
        }

        Vendor vendor = new Vendor();
        vendor.setName(dto.getName());
        vendor.setEmail(dto.getEmail());
        vendor.setPhone(dto.getPhone());
        vendor.setAddress(dto.getAddress());
        vendor.setActive(true);

        return vendorRepository.save(vendor);
    }

    public Vendor getById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + id));
    }

    public List<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    @Transactional
    public Vendor update(Long id, VendorDTO dto) {
        Vendor vendor = getById(id);
        vendor.setName(dto.getName());
        vendor.setEmail(dto.getEmail());
        vendor.setPhone(dto.getPhone());
        vendor.setAddress(dto.getAddress());
        vendor.setActive(dto.isActive());
        return vendorRepository.save(vendor);
    }

    @Transactional
    public void deactivate(Long id) {
        Vendor vendor = getById(id);
        vendor.setActive(false);
        vendorRepository.save(vendor);
    }
}
