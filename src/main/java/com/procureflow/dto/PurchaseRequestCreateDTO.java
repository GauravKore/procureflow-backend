package com.procureflow.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PurchaseRequestCreateDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<PurchaseRequestItemDTO> items;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<PurchaseRequestItemDTO> getItems() { return items; }
    public void setItems(List<PurchaseRequestItemDTO> items) { this.items = items; }
}
