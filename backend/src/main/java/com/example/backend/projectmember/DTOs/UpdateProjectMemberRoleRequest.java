package com.example.backend.projectmember.DTOs;

import com.example.backend.projectmember.ProjectRole;

import jakarta.validation.constraints.NotNull;

public record UpdateProjectMemberRoleRequest(
    @NotNull ProjectRole role
) {}