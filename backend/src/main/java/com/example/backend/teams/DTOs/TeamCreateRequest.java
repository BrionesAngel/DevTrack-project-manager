package com.example.backend.teams.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamCreateRequest(
  @NotBlank @Size(min = 2, max = 50, message = "team name min 2 chars, max 50") String name
) {}
