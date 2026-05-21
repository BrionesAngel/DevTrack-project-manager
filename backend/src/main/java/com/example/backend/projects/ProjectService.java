package com.example.backend.projects;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.projectmember.ProjectMember;
import com.example.backend.projectmember.ProjectRole;
import com.example.backend.projectmember.DTOs.ProjectMemberOverview;
import com.example.backend.projects.DTOs.ProjectCreateRequest;
import com.example.backend.projects.DTOs.ProjectCreateResponse;
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
  private final ProjectAuthorizationService projectAuthorizationService;

  public Project getProjectIfUserHasPermission(Long userId, Long projectId) {
    return projectRepository.findProjectIfUserHasPermission(projectId, userId)
        .orElseThrow(
            () -> new ResourceNotFoundException("user " + userId + " has no permission on project " + projectId));
  }

  public ProjectResponse getProjectIfUserIsMember(Long userId, Long projectId) {
    Project project = projectRepository.findByIdAndMemberId(projectId, userId)
        .orElseThrow(() -> new ResourceNotFoundException("user " + userId + " has no permission on project " + projectId));

    List<TeamOverview> teams = project.getTeams().stream()
        .map(t -> {
          List<TeamMemberOverview> members = t.getMembers().stream()
              .map(m -> new TeamMemberOverview(m.getUser().getId(), m.getUser().getUsername(), m.getRole()))
              .toList();
          return new TeamOverview(t.getId(), t.getName(), members);
        })
        .toList();

    List<ProjectMemberOverview> members = project.getMembers().stream()
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
}
