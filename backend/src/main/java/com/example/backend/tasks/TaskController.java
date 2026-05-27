package com.example.backend.tasks;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.auth.security.CustomUserDetails;
import com.example.backend.tasks.DTOs.TaskAssignUserRequest;
import com.example.backend.tasks.DTOs.TaskCommentCreateRequest;
import com.example.backend.tasks.DTOs.TaskCommentResponse;
import com.example.backend.tasks.DTOs.TaskCreateRequest;
import com.example.backend.tasks.DTOs.TaskOverviewResponse;
import com.example.backend.tasks.DTOs.TaskUpdateRequest;

import jakarta.validation.Valid;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class TaskController {
  private final TaskService taskService;

  @PostMapping("/projects/{projectId}/tasks")
  @ResponseStatus(HttpStatus.CREATED)
  public TaskOverviewResponse createTaskOnProject(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId,
      @Valid @RequestBody TaskCreateRequest request) {
    return taskService.createTask(userDetails.getUser(), projectId, request);
  }

  @GetMapping("/projects/{projectId}/tasks")
  public List<TaskOverviewResponse> getProjectTasks(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId,
      @RequestParam(required = false) Long teamId,
      @RequestParam(required = false) Long assignedUserId) {
    return taskService.getProjectTasks(userDetails.getUser().getId(), projectId, teamId, assignedUserId);
  }

  @PatchMapping("/projects/{projectId}/tasks/{taskId}/assignee")
  public TaskOverviewResponse updateTaskAssignee(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId,
      @PathVariable Long taskId,
      @Valid @RequestBody TaskAssignUserRequest request) {
    return taskService.updateTaskAssignee(userDetails.getUser().getId(), projectId, taskId, request);
  }

  @PatchMapping("/projects/{projectId}/tasks/{taskId}")
  public TaskOverviewResponse updateTask(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId,
      @PathVariable Long taskId,
      @RequestBody TaskUpdateRequest request) {
    return taskService.updateTask(userDetails.getUser().getId(), projectId, taskId, request);
  }

  @GetMapping("/projects/{projectId}/tasks/{taskId}/comments")
  public List<TaskCommentResponse> getTaskComments(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId,
      @PathVariable Long taskId) {
    return taskService.getTaskComments(userDetails.getUser().getId(), projectId, taskId);
  }

  @PostMapping("/projects/{projectId}/tasks/{taskId}/comments")
  @ResponseStatus(HttpStatus.CREATED)
  public TaskCommentResponse addTaskComment(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId,
      @PathVariable Long taskId,
      @Valid @RequestBody TaskCommentCreateRequest request) {
    return taskService.addTaskComment(userDetails.getUser().getId(), projectId, taskId, request);
  }
}
