package com.example.backend.tasks;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.auth.security.CustomUserDetails;
import com.example.backend.tasks.DTOs.TaskCreateRequest;
import com.example.backend.tasks.DTOs.TaskCreateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class TaskController {
  private final TaskService taskService;

  @PostMapping("/projects/{projectId}/tasks")
  @ResponseStatus(HttpStatus.CREATED)
  public TaskCreateResponse createTaskOnProject(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId,
      @RequestBody TaskCreateRequest request) {
    return taskService.createTask(userDetails.getUser(), projectId, request);
  }
}
