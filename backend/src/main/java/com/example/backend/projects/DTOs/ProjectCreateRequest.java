package com.example.backend.projects.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
  @NotBlank(message = "title is required") @Size(min=2, max=100, message = "title must contain between 2 and 100 characters") String title,
  @NotBlank(message = "description is required") @Size(min=2, max=1000, message = "description must contain between 2 and 1000 characters") String description
) {}
