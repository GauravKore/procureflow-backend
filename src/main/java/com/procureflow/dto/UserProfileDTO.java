package com.procureflow.dto;

import com.procureflow.entity.User;

public class UserProfileDTO {

    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean active;

    public static UserProfileDTO fromEntity(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.role = user.getRole().getName();
        dto.active = user.isActive();
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
}
