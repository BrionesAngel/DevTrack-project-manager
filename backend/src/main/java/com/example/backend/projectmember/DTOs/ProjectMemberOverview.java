package com.example.backend.projectmember.DTOs;

import com.example.backend.projectmember.ProjectRole;

public record ProjectMemberOverview(
  Long userId,
  String username,
  ProjectRole role
) {}
