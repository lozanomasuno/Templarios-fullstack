package com.templarios.backend.application.dto;

import com.templarios.backend.domain.model.RecordStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RecordRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    private RecordStatus status;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public RecordStatus getStatus() { return status; }
    public void setStatus(RecordStatus status) { this.status = status; }
}
