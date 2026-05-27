package com.example.backend.notifications.DTOs;

import java.time.LocalDateTime;

import com.example.backend.notifications.NotificationStatus;
import com.example.backend.notifications.NotificationType;
import com.example.backend.notifications.ResourceType;

public record NotificationResponse(
    Long id,
    Long senderId,
    String senderUsername,
    Long recipientId,
    String recipientUsername,
    NotificationType type,
    ResourceType resourceType,
    Long resourceId,
    NotificationStatus status,
    LocalDateTime createdAt
) {}