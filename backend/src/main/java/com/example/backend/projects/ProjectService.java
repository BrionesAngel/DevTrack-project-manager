package com.example.backend.projects;

import org.springframework.stereotype.Service;

import com.example.backend.projects.DTOs.ProjectCreateRequest;
import com.example.backend.projects.DTOs.ProjectCreateResponse;
import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.users.User;
import com.example.backend.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  public ProjectCreateResponse createProject(String email, ProjectCreateRequest request) {
    User user = userRepository.findByEmail(email);
    if (user == null)
      throw new ResourceNotFoundException("User not found");

    Project project = Project.builder()
        .title(request.title())
        .description(request.description())
        .createdBy(user)
        .build();
    projectRepository.save(project);

    return new ProjectCreateResponse(project.getId(), project.getTitle(), project.getDescription(),
        project.getCreatedBy().getUsername());
  }
}
