package com.example.backend.teams.DTOs;

import java.util.List;

import com.example.backend.projectmember.DTOs.ProjectMemberOverview;
import com.example.backend.teammembers.DTOs.TeamMemberOverview;

public record TeamDetailsResponse(
  Long id,
  String name,
  Long projectId,
  List<TeamMemberOverview> members,
  List<ProjectMemberOverview> projectMembers
) {}