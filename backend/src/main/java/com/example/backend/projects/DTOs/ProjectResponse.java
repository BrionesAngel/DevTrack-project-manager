package com.example.backend.projects.DTOs;

import java.util.List;

import com.example.backend.projectmember.DTOs.ProjectMemberOverview;
import com.example.backend.teams.DTOs.TeamOverview;

public record ProjectResponse(
  Long id,
  String title,
  String description,
  String createdBy,
  List<TeamOverview> teams,
  List<ProjectMemberOverview> members
) {}
