package com.example.backend.projects.DTOs;

public record ProjectOverviewResponse(
    Long id,
    String title,
    String description,
    String createdBy,
    int teams,
    int members) {
}
