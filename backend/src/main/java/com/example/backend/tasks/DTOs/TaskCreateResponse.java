package com.example.backend.tasks.DTOs;

import java.time.LocalDate;

import com.example.backend.tasks.TaskPriority;
import com.example.backend.tasks.TaskStatus;

public record TaskCreateResponse(
  Long id,
  String title,
  String description,
  TaskPriority priority,
  TaskStatus status,
  LocalDate dueDate,
  Long assignedUser,
  Long assignedTeam,
  Long project
) {}
