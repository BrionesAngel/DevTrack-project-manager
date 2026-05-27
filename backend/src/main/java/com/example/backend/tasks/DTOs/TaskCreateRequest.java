package com.example.backend.tasks.DTOs;

import java.time.LocalDate;

import com.example.backend.tasks.TaskPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCreateRequest(
  @NotBlank String title,
  @NotBlank String description,
  TaskPriority priority,
  LocalDate dueDate,
  String githubIssueUrl,
  String comment,
  Long assignedUser,
  @NotNull Long teamId
) {}
