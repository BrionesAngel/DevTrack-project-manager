package com.example.backend.teammembers.DTOs;

import com.example.backend.teammembers.TeamRole;

import jakarta.validation.constraints.NotNull;

public record UpdateTeamMemberRoleRequest(
  @NotNull TeamRole role
) {}