package com.example.backend.notifications.DTOs;

import com.example.backend.notifications.NotificationType;
import com.example.backend.notifications.ResourceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationCreateRequest(
    @NotBlank String username,
    @NotNull NotificationType type,
    @NotNull ResourceType resourceType,
    @NotNull Long resourceId
) {}
