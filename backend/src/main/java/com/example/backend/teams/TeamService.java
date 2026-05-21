package com.example.backend.teams;

import org.springframework.stereotype.Service;

import com.example.backend.projects.Project;
import com.example.backend.projects.ProjectService;
import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.teammembers.TeamMember;
import com.example.backend.teammembers.TeamRole;
import com.example.backend.teams.DTOs.TeamCreateRequest;
import com.example.backend.teams.DTOs.TeamCreateResponse;
import com.example.backend.users.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
  private final TeamRepository teamRepository;
  private final ProjectService projectService;

  public Team getTeamInProject(Long teamId, Long projectId) {
    return teamRepository.findByIdAndProjectId(teamId, projectId)
      .orElseThrow(() -> new ResourceNotFoundException("team " + teamId + " has not been found on project " + projectId));
  }

  public TeamCreateResponse createTeam(User user, TeamCreateRequest request, Long projectId) {
    Project project = projectService.getProjectIfUserHasPermission(user.getId(), projectId);

    Team team = Team.builder()
        .name(request.name())
        .project(project)
        .build();

    TeamMember member = TeamMember.builder()
        .user(user)
        .team(team)
        .role(TeamRole.LEAD)
        .build();

    team.getMembers().add(member);

    teamRepository.save(team);

    return new TeamCreateResponse(team.getId(), team.getName(), project.getId());
  }
}
