package com.example.backend.projects.DTOs;

public record ProjectCreateResponse(
    Long id,
    String title,
    String description,
    String createdBy
) {
}
