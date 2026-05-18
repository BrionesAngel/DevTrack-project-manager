package com.example.backend.projects;

import org.springframework.stereotype.Service;

import com.example.backend.projectmember.ProjectMember;
import com.example.backend.projectmember.ProjectMemberRepository;
import com.example.backend.projectmember.ProjectRole;
import com.example.backend.projects.DTOs.ProjectCreateRequest;
import com.example.backend.projects.DTOs.ProjectCreateResponse;
import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.users.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final ProjectMemberRepository projectMemberRepository;
  private final ProjectAuthorizationService projectAuthorizationService;

  public ProjectCreateResponse createProject(User user, ProjectCreateRequest request) {
    Project project = Project.builder()
        .title(request.title())
        .description(request.description())
        .createdBy(user)
        .build();
    projectRepository.save(project);

    ProjectMember projectMember = ProjectMember.builder()
        .user(user)
        .project(project)
        .role(ProjectRole.OWNER)
        .build();
    projectMemberRepository.save(projectMember);

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
