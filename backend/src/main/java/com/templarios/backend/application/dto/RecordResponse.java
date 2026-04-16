package com.templarios.backend.application.dto;

import com.templarios.backend.domain.model.RecordStatus;

import java.time.LocalDateTime;

public class RecordResponse {

    private Long id;
    private String title;
    private String description;
    private RecordStatus status;
    private String ownerUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecordResponse(Long id, String title, String description, RecordStatus status,
                          String ownerUsername, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public RecordStatus getStatus() { return status; }
    public String getOwnerUsername() { return ownerUsername; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
