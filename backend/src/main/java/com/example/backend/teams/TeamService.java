package com.example.backend.teams;

import java.util.Comparator;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.projectmember.ProjectMember;
import com.example.backend.projectmember.ProjectMemberRepository;
import com.example.backend.projects.Project;
import com.example.backend.projects.ProjectAuthorizationService;
import com.example.backend.projects.ProjectService;
import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.teammembers.TeamMember;
import com.example.backend.teammembers.TeamMemberRepository;
import com.example.backend.teammembers.TeamRole;
import com.example.backend.teammembers.DTOs.TeamMemberOverview;
import com.example.backend.projectmember.DTOs.ProjectMemberOverview;
import com.example.backend.teams.DTOs.TeamCreateRequest;
import com.example.backend.teams.DTOs.TeamCreateResponse;
import com.example.backend.teams.DTOs.TeamDetailsResponse;
import com.example.backend.teammembers.DTOs.UpdateTeamMemberRoleRequest;
import com.example.backend.tasks.TaskRepository;
import com.example.backend.users.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
  private final TeamRepository teamRepository;
  private final ProjectService projectService;
  private final ProjectAuthorizationService projectAuthorizationService;
  private final TeamMemberRepository teamMemberRepository;
  private final ProjectMemberRepository projectMemberRepository;
  private final TaskRepository taskRepository;

  public Team getTeamInProject(Long teamId, Long projectId) {
    return teamRepository.findByIdAndProjectId(teamId, projectId)
        .orElseThrow(
            () -> new ResourceNotFoundException("team " + teamId + " has not been found on project " + projectId));
  }

  @Transactional
  public void deleteTeam(Long userId, Long projectId, Long teamId) {
    projectAuthorizationService.validateAdmin(userId, projectId);
    Team team = this.getTeamInProject(teamId, projectId);
    taskRepository.clearAssignedTeamByTeamId(team.getId());
    teamRepository.delete(team);
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

  public TeamDetailsResponse getTeamDetails(Long userId, Long projectId, Long teamId) {
    // ensure user is at least a project member
    ProjectMember projectMember = projectMemberRepository.findByUserIdAndProjectId(userId, projectId)
        .orElseThrow(() -> new AccessDeniedException("Not a member of this project"));

    Team team = this.getTeamInProject(teamId, projectId);

    // allow owners/admins or team members
    if (projectMember.getRole() != com.example.backend.projectmember.ProjectRole.OWNER
        && projectMember.getRole() != com.example.backend.projectmember.ProjectRole.ADMIN) {
      boolean isTeamMember = teamMemberRepository.existsByUserIdAndTeamId(userId, team.getId());
      if (!isTeamMember) {
        throw new AccessDeniedException("Not a member of this team");
      }
    }
    List<TeamMemberOverview> members = teamMemberRepository.findAllByTeamId(team.getId()).stream()
        .sorted(Comparator.comparing(TeamMember::getId))
        .map(member -> new TeamMemberOverview(member.getUser().getId(), member.getUser().getUsername(), member.getRole()))
        .toList();

    List<Long> teamMemberIds = members.stream().map(TeamMemberOverview::userId).toList();
    List<ProjectMemberOverview> projectMembers = projectMemberRepository.findAllByProjectId(projectId).stream()
        .sorted(Comparator.comparing(ProjectMember::getId))
        .filter(member -> !teamMemberIds.contains(member.getUser().getId()))
        .map(member -> new ProjectMemberOverview(member.getUser().getId(), member.getUser().getUsername(), member.getRole()))
        .toList();

    return new TeamDetailsResponse(team.getId(), team.getName(), projectId, members, projectMembers);
  }

  @Transactional
  public TeamMemberOverview upsertTeamMemberRole(Long userId, Long projectId, Long teamId, Long targetUserId, UpdateTeamMemberRoleRequest request) {
    // Only project owners/admins can add/update team members
    projectAuthorizationService.validateAdmin(userId, projectId);

    Team team = this.getTeamInProject(teamId, projectId);
    ProjectMember projectMember = projectMemberRepository.findByUserIdAndProjectId(targetUserId, projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project member not found"));

    TeamMember teamMember = teamMemberRepository.findByUserIdAndTeamId(targetUserId, team.getId())
        .orElseGet(() -> TeamMember.builder()
            .user(projectMember.getUser())
            .team(team)
            .role(request.role())
            .build());

    if (teamMember.getId() != null && teamMember.getRole() == TeamRole.LEAD && request.role() != TeamRole.LEAD) {
      long leadCount = teamMemberRepository.countByTeamIdAndRole(team.getId(), TeamRole.LEAD);
      if (leadCount <= 1) {
        throw new AccessDeniedException("Cannot remove the last team lead");
      }
    }

    if (request.role() == TeamRole.LEAD) {
      teamMemberRepository.findAllByTeamId(team.getId()).stream()
          .filter(member -> !member.getUser().getId().equals(targetUserId) && member.getRole() == TeamRole.LEAD)
          .forEach(member -> member.setRole(TeamRole.MEMBER));
    }

    teamMember.setRole(request.role());
    TeamMember savedTeamMember = teamMemberRepository.save(teamMember);

    return new TeamMemberOverview(
        savedTeamMember.getUser().getId(),
        savedTeamMember.getUser().getUsername(),
        savedTeamMember.getRole());
  }
}
