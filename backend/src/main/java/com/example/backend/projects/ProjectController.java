package com.example.backend.projects;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.auth.security.CustomUserDetails;
import com.example.backend.projects.DTOs.ProjectCreateRequest;
import com.example.backend.projects.DTOs.ProjectCreateResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor

public class ProjectController {
  private final ProjectService projectService;

  @PostMapping
  public ProjectCreateResponse createProject(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody ProjectCreateRequest request) {
    return projectService.createProject(userDetails.getUser(), request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProject(
    @AuthenticationPrincipal CustomUserDetails userDetails,
    @PathVariable Long id
  ){
    projectService.deleteProject(userDetails.getId(), id);
  }
}
