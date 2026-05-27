package com.example.backend.projects.DTOs;

import java.util.List;

import com.example.backend.teams.DTOs.TeamOverview;

public record ProjectOverviewTeamsResponse(
    Long id,
    String title,
    String createdBy,
    List<TeamOverview> teams
) {
}
