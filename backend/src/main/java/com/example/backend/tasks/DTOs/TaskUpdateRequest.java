package com.example.backend.tasks.DTOs;

import java.time.LocalDate;

import com.example.backend.tasks.TaskPriority;
import com.example.backend.tasks.TaskStatus;

public record TaskUpdateRequest(
    TaskPriority priority,
    TaskStatus status,
    LocalDate dueDate,
    Long assignedUserId,
    String githubIssueUrl,
    String comment) {
}
