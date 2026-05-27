package com.example.backend.tasks.DTOs;

import jakarta.validation.constraints.NotNull;

public record TaskAssignUserRequest(
    @NotNull Long assignedUserId) {
}
