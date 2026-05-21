package com.example.backend.teams.DTOs;

import java.util.List;

import com.example.backend.teammembers.DTOs.TeamMemberOverview;

public record TeamOverview(
  Long teamId,
  String name,
  List<TeamMemberOverview> members
) {}
