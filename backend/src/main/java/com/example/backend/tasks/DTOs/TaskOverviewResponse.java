package com.example.backend.tasks.DTOs;

import java.time.LocalDate;

import com.example.backend.tasks.TaskPriority;
import com.example.backend.tasks.TaskStatus;

public record TaskOverviewResponse(
    Long id,
    String title,
    String description,
    TaskPriority priority,
    TaskStatus status,
    LocalDate dueDate,
    Long assignedUserId,
    String assignedUsername,
    Long assignedTeamId,
    String assignedTeamName,
    Long projectId,
    String githubIssueUrl) {
}
