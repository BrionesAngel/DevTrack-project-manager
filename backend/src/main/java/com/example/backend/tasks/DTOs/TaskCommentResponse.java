package com.example.backend.tasks.DTOs;

import java.time.LocalDateTime;

public record TaskCommentResponse(
    Long id,
    Long authorId,
    String authorUsername,
    String action,
    String message,
    LocalDateTime createdAt) {
}
