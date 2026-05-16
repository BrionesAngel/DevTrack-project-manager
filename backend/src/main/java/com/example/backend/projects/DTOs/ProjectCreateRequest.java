package com.example.backend.projects.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
  @NotBlank(message = "title is required") @Size(min=2, max=30) String title,              
  @NotBlank(message = "description is required") @Size(min=2, max=1000) String description
) {}
