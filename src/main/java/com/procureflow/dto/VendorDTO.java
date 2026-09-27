package com.procureflow.dto;

import com.procureflow.entity.Vendor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VendorDTO {

    private Long id;

    @NotBlank(message = "Vendor name is required")
    private String name;

    @NotBlank(message = "Vendor email is required")
    @Email(message = "Vendor email must be valid")
    private String email;

    private String phone;
    private String address;
    private boolean active = true;

    public static VendorDTO fromEntity(Vendor v) {
        VendorDTO dto = new VendorDTO();
        dto.id = v.getId();
        dto.name = v.getName();
        dto.email = v.getEmail();
        dto.phone = v.getPhone();
        dto.address = v.getAddress();
        dto.active = v.isActive();
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
