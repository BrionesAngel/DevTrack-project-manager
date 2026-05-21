package com.example.backend.teammembers.DTOs;

import com.example.backend.teammembers.TeamRole;

public record TeamMemberOverview(
  Long userId,
  String username,
  TeamRole role
) {}
