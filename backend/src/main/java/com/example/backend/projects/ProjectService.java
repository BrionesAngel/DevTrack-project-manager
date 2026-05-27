package com.example.backend.projects;

import java.util.List;
import java.util.Comparator;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.example.backend.projectmember.ProjectMember;
import com.example.backend.projectmember.ProjectMemberRepository;
import com.example.backend.projectmember.ProjectRole;
import com.example.backend.projectmember.DTOs.ProjectMemberOverview;
import com.example.backend.projects.DTOs.ProjectCreateRequest;
import com.example.backend.projects.DTOs.ProjectCreateResponse;
import com.example.backend.projects.DTOs.ProjectOverviewResponse;
import com.example.backend.projects.DTOs.ProjectOverviewTeamsResponse;
import com.example.backend.projects.DTOs.ProjectResponse;
import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.teammembers.DTOs.TeamMemberOverview;
import com.example.backend.teams.DTOs.TeamOverview;
import com.example.backend.users.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final ProjectMemberRepository projectMemberRepository;
  private final ProjectAuthorizationService projectAuthorizationService;

  public List<ProjectOverviewResponse> getAllProjects(Long userId) {
    List<Project> projects = projectRepository.findAllByMemberId(userId);

    return projects.stream()
        .map(this::toProjectOverviewResponse)
        .toList();
  }

  public List<ProjectOverviewTeamsResponse> getAllProjectsWithTeams(Long userId) {
    List<Project> projects = projectRepository.findAllByMemberId(userId);

    return projects.stream()
        .map(project -> new ProjectOverviewTeamsResponse(
            project.getId(),
            project.getTitle(),
            project.getCreatedBy().getUsername(),
            project.getTeams().stream()
                .map(team -> new TeamOverview(
                    team.getId(),
                    team.getName(),
                    team.getMembers().size()))
                .toList()))
        .toList();
  }

  private ProjectOverviewResponse toProjectOverviewResponse(Project project) {
    return new ProjectOverviewResponse(
        project.getId(),
        project.getTitle(),
        project.getCreatedBy().getUsername(),
        project.getTeams().size(),
        project.getMembers().size());
  }

  private ProjectResponse toProjectResponse(Project project) {
    List<TeamOverview> teams = project.getTeams().stream()
        .map(t -> {
          List<TeamMemberOverview> members = t.getMembers().stream()
              .map(m -> new TeamMemberOverview(m.getUser().getId(), m.getUser().getUsername(), m.getRole()))
              .toList();
          return new TeamOverview(t.getId(), t.getName(), members.size());
        })
        .toList();

    List<ProjectMemberOverview> members = project.getMembers().stream()
      .sorted(Comparator.comparing(ProjectMember::getId))
        .map(m -> new ProjectMemberOverview(m.getUser().getId(), m.getUser().getUsername(), m.getRole()))
        .toList();

    return new ProjectResponse(
        project.getId(),
        project.getTitle(),
        project.getDescription(),
        project.getCreatedBy().getUsername(),
        teams,
        members);

  }

  public Project getProjectIfUserHasPermission(Long userId, Long projectId) {
    return projectRepository.findProjectIfUserHasPermission(projectId, userId)
        .orElseThrow(
            () -> new ResourceNotFoundException("user " + userId + " has no permission on project " + projectId));
  }

  public ProjectResponse getProjectIfUserIsMember(Long userId, Long projectId) {
    Project project = projectRepository.findByIdAndMemberId(projectId, userId)
        .orElseThrow(
            () -> new ResourceNotFoundException("user " + userId + " has no permission on project " + projectId));
    return toProjectResponse(project);
  }

  public ProjectCreateResponse createProject(User user, ProjectCreateRequest request) {
    Project project = Project.builder()
        .title(request.title())
        .description(request.description())
        .createdBy(user)
        .build();

    ProjectMember projectMember = ProjectMember.builder()
        .user(user)
        .project(project)
        .role(ProjectRole.OWNER)
        .build();

    project.getMembers().add(projectMember);

    projectRepository.save(project);

    return new ProjectCreateResponse(project.getId(), project.getTitle(), project.getDescription(),
        project.getCreatedBy().getUsername());
  }

  public void deleteProject(Long userId, Long id) {
    Project project = projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

    projectAuthorizationService.validateOwner(userId, project.getId());

    projectRepository.delete(project);
  }

  @Transactional
  public ProjectMemberOverview updateProjectMemberRole(Long actorUserId, Long projectId, Long targetUserId, ProjectRole role) {
    projectAuthorizationService.validateOwner(actorUserId, projectId);

    ProjectMember targetMember = projectMemberRepository.findByUserIdAndProjectId(targetUserId, projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project member not found"));

    if (targetMember.getRole() == ProjectRole.OWNER && role != ProjectRole.OWNER) {
      long ownerCount = projectMemberRepository.countByProjectIdAndRole(projectId, ProjectRole.OWNER);
      if (ownerCount <= 1) {
        throw new AccessDeniedException("Cannot remove the last project owner");
      }
    }

    if (role == ProjectRole.OWNER) {
      projectMemberRepository.findAllByProjectIdAndRole(projectId, ProjectRole.OWNER).stream()
          .filter(member -> !member.getUser().getId().equals(targetUserId))
          .forEach(member -> member.setRole(ProjectRole.ADMIN));
    }

    targetMember.setRole(role);
    projectMemberRepository.save(targetMember);

    return new ProjectMemberOverview(
        targetMember.getUser().getId(),
        targetMember.getUser().getUsername(),
        targetMember.getRole());
  }
}
