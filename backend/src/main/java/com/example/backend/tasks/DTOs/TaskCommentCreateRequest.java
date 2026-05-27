package com.example.backend.tasks.DTOs;

import jakarta.validation.constraints.NotBlank;

public record TaskCommentCreateRequest(
    @NotBlank String message) {
}
