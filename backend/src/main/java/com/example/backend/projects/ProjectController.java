package com.example.backend.projects;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.auth.security.CustomUserDetails;
import com.example.backend.projectmember.DTOs.ProjectMemberOverview;
import com.example.backend.projectmember.DTOs.UpdateProjectMemberRoleRequest;
import com.example.backend.projects.DTOs.ProjectCreateRequest;
import com.example.backend.projects.DTOs.ProjectCreateResponse;
import com.example.backend.projects.DTOs.ProjectOverviewResponse;
import com.example.backend.projects.DTOs.ProjectOverviewTeamsResponse;
import com.example.backend.projects.DTOs.ProjectResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor

public class ProjectController {
  private final ProjectService projectService;

  @GetMapping("/{id}")
  public ProjectResponse getProject(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
    return projectService.getProjectIfUserIsMember(userDetails.getUser().getId(), id);
  }

  @GetMapping
  public List<ProjectOverviewResponse> getAllProjects(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return projectService.getAllProjects(userDetails.getUser().getId());
  }

  @GetMapping("/with-teams")
  public List<ProjectOverviewTeamsResponse> getAllProjectsWithTeams(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return projectService.getAllProjectsWithTeams(userDetails.getUser().getId());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProjectCreateResponse createProject(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody ProjectCreateRequest request) {
    return projectService.createProject(userDetails.getUser(), request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProject(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id) {
    projectService.deleteProject(userDetails.getId(), id);
  }

  @PatchMapping("/{projectId}/members/{userId}/role")
  public ProjectMemberOverview updateProjectMemberRole(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId,
      @PathVariable Long userId,
      @Valid @RequestBody UpdateProjectMemberRoleRequest request) {
    return projectService.updateProjectMemberRole(userDetails.getUser().getId(), projectId, userId, request.role());
  }
}
