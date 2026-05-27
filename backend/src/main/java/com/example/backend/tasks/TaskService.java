package com.example.backend.tasks;

import java.time.LocalDate;
import java.util.Locale;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.backend.projectmember.ProjectMember;
import com.example.backend.projectmember.ProjectRole;
import com.example.backend.projects.ProjectAuthorizationService;
import com.example.backend.projects.Project;
import com.example.backend.projects.ProjectService;
import com.example.backend.notifications.NotificationService;
import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.tasks.DTOs.TaskAssignUserRequest;
import com.example.backend.tasks.DTOs.TaskCommentCreateRequest;
import com.example.backend.tasks.DTOs.TaskCommentResponse;
import com.example.backend.tasks.DTOs.TaskCreateRequest;
import com.example.backend.tasks.DTOs.TaskOverviewResponse;
import com.example.backend.tasks.DTOs.TaskUpdateRequest;
import com.example.backend.teammembers.TeamMember;
import com.example.backend.teammembers.TeamMemberService;
import com.example.backend.teammembers.TeamMemberRepository;
import com.example.backend.teammembers.TeamRole;
import com.example.backend.teams.Team;
import com.example.backend.teams.TeamService;
import com.example.backend.users.User;
import com.example.backend.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final ProjectService projectService;
  private final ProjectAuthorizationService projectAuthorizationService;
  private final TeamService teamService;
  private final TeamMemberService teamMemberService;
  private final TeamMemberRepository teamMemberRepository;
  private final TaskCommentRepository taskCommentRepository;
  private final UserRepository userRepository;
  private final NotificationService notificationService;

  public TaskOverviewResponse createTask(User user, Long projectId, TaskCreateRequest request) {
    projectAuthorizationService.validateAdmin(user.getId(), projectId);

    Project project = projectService.getProjectIfUserHasPermission(user.getId(), projectId);
    Team team = teamService.getTeamInProject(request.teamId(), projectId);
    User assignedUser = request.assignedUser() != null
        ? teamMemberService.getUserIfIsTeamMember(request.assignedUser(), team.getId())
        : null;

    TaskPriority priority = request.priority() != null ? request.priority() : TaskPriority.LOW;
    TaskStatus status = assignedUser != null ? TaskStatus.ASSIGNED : TaskStatus.UNASSIGNED;
    LocalDate dueDate = request.dueDate() != null ? request.dueDate() : null;

    Task task = Task.builder()
        .title(request.title())
        .description(request.description())
        .priority(priority)
        .status(status)
        .dueDate(dueDate)
        .githubIssueUrl(normalizeIssueUrl(request.githubIssueUrl()))
        .assignedUser(assignedUser)
        .assignedTeam(team)
        .project(project)
        .build();

      Task saved = taskRepository.save(task);
      if (assignedUser != null && !assignedUser.getId().equals(user.getId())) {
        notificationService.createTaskAssignedNotification(user, assignedUser, saved.getId());
      }
      addCommentInternal(saved, user, "CREATED", request.comment());
      return toOverview(saved);
  }

  public List<TaskOverviewResponse> getProjectTasks(Long userId, Long projectId, Long teamId, Long assignedUserId) {
    projectAuthorizationService.validateMember(userId, projectId);

    List<Task> tasks;
    if (teamId != null && assignedUserId != null) {
      tasks = taskRepository.findAllByProjectIdAndAssignedTeamIdAndAssignedUserIdOrderByIdAsc(projectId, teamId, assignedUserId);
    } else if (teamId != null) {
      tasks = taskRepository.findAllByProjectIdAndAssignedTeamIdOrderByIdAsc(projectId, teamId);
    } else if (assignedUserId != null) {
      tasks = taskRepository.findAllByProjectIdAndAssignedUserIdOrderByIdAsc(projectId, assignedUserId);
    } else {
      tasks = taskRepository.findAllByProjectIdOrderByIdAsc(projectId);
    }

    return tasks.stream()
        .map(this::toOverview)
        .toList();
  }

  public TaskOverviewResponse updateTaskAssignee(Long actorUserId, Long projectId, Long taskId, TaskAssignUserRequest request) {
    User actor = getActor(actorUserId);
    Task task = getTaskInProject(taskId, projectId);
    validateCanReassign(actorUserId, projectId, task);

    User assignedUser = teamMemberService.getUserIfIsTeamMember(request.assignedUserId(), task.getAssignedTeam().getId());
    task.setAssignedUser(assignedUser);

    if (task.getStatus() == TaskStatus.UNASSIGNED) {
      task.setStatus(TaskStatus.ASSIGNED);
    }

    Task saved = taskRepository.save(task);
    if (!assignedUser.getId().equals(actor.getId())) {
      notificationService.createTaskAssignedNotification(actor, assignedUser, saved.getId());
    }
    addCommentInternal(saved, actor, "ASSIGNED", "Assigned to " + assignedUser.getUsername());
    return toOverview(saved);
  }

  public TaskOverviewResponse updateTask(Long actorUserId, Long projectId, Long taskId, TaskUpdateRequest request) {
    projectAuthorizationService.validateMember(actorUserId, projectId);
    User actor = getActor(actorUserId);

    Task task = getTaskInProject(taskId, projectId);

    boolean requestsAdminFields = request.priority() != null
        || request.dueDate() != null
        || request.assignedUserId() != null
        || request.githubIssueUrl() != null;

    if (requestsAdminFields) {
      projectAuthorizationService.validateAdmin(actorUserId, projectId);
    }

    if (request.priority() != null) {
      task.setPriority(request.priority());
    }

    if (request.dueDate() != null) {
      task.setDueDate(request.dueDate());
    }

    if (request.githubIssueUrl() != null) {
      task.setGithubIssueUrl(normalizeIssueUrl(request.githubIssueUrl()));
    }

    if (request.assignedUserId() != null) {
      User assignedUser = teamMemberService.getUserIfIsTeamMember(request.assignedUserId(), task.getAssignedTeam().getId());
      task.setAssignedUser(assignedUser);
      if (task.getStatus() == TaskStatus.UNASSIGNED) {
        task.setStatus(TaskStatus.ASSIGNED);
      }
      if (!assignedUser.getId().equals(actor.getId())) {
        notificationService.createTaskAssignedNotification(actor, assignedUser, task.getId());
      }
    }

    if (request.status() != null) {
      validateStatusTransition(actorUserId, task, request.status());
      task.setStatus(request.status());
    }

    Task saved = taskRepository.save(task);
    if (request.status() != null) {
      addCommentInternal(saved, actor, "STATUS_" + request.status().name(), request.comment());
    } else {
      addCommentInternal(saved, actor, "UPDATED", request.comment());
    }

    return toOverview(saved);
  }

  public List<TaskCommentResponse> getTaskComments(Long actorUserId, Long projectId, Long taskId) {
    projectAuthorizationService.validateMember(actorUserId, projectId);
    Task task = getTaskInProject(taskId, projectId);

    return taskCommentRepository.findAllByTaskIdOrderByCreatedAtAsc(task.getId())
        .stream()
        .map(this::toCommentResponse)
        .toList();
  }

  public TaskCommentResponse addTaskComment(Long actorUserId, Long projectId, Long taskId, TaskCommentCreateRequest request) {
    projectAuthorizationService.validateMember(actorUserId, projectId);
    User actor = getActor(actorUserId);
    Task task = getTaskInProject(taskId, projectId);

    TaskComment comment = TaskComment.builder()
        .task(task)
        .author(actor)
        .action("COMMENT")
        .message(request.message().trim())
        .build();

    TaskComment saved = taskCommentRepository.save(comment);
    return toCommentResponse(saved);
  }

  private Task getTaskInProject(Long taskId, Long projectId) {
    return taskRepository.findByIdAndProjectId(taskId, projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found in project"));
  }

  private TaskOverviewResponse toOverview(Task task) {
    return new TaskOverviewResponse(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getPriority(),
        task.getStatus(),
        task.getDueDate(),
        task.getAssignedUser() != null ? task.getAssignedUser().getId() : null,
        task.getAssignedUser() != null ? task.getAssignedUser().getUsername() : null,
        task.getAssignedTeam().getId(),
        task.getAssignedTeam().getName(),
        task.getProject().getId(),
        task.getGithubIssueUrl());
  }

  private TaskCommentResponse toCommentResponse(TaskComment comment) {
    return new TaskCommentResponse(
        comment.getId(),
        comment.getAuthor().getId(),
        comment.getAuthor().getUsername(),
        comment.getAction(),
        comment.getMessage(),
        comment.getCreatedAt());
  }

  private User getActor(Long actorUserId) {
    return userRepository.findById(actorUserId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private void validateStatusTransition(Long actorUserId, Task task, TaskStatus newStatus) {
    TaskStatus currentStatus = task.getStatus();
    boolean isAssignee = task.getAssignedUser() != null && task.getAssignedUser().getId().equals(actorUserId);
    TeamMember teamMember = teamMemberRepository.findByUserIdAndTeamId(actorUserId, task.getAssignedTeam().getId())
        .orElse(null);
    boolean isLead = teamMember != null && teamMember.getRole() == TeamRole.LEAD;

    if ((currentStatus == TaskStatus.ASSIGNED || currentStatus == TaskStatus.REJECTED) && newStatus == TaskStatus.IN_PROGRESS) {
      if (!isAssignee) {
        throw new AccessDeniedException("Only assigned member can take this task");
      }
      return;
    }

    if (currentStatus == TaskStatus.IN_PROGRESS && newStatus == TaskStatus.REVIEW) {
      if (!isAssignee) {
        throw new AccessDeniedException("Only assigned member can request review");
      }
      return;
    }

    if (currentStatus == TaskStatus.REVIEW && (newStatus == TaskStatus.COMPLETED || newStatus == TaskStatus.REJECTED)) {
      if (!isLead) {
        throw new AccessDeniedException("Only team lead can review task");
      }
      return;
    }

    throw new AccessDeniedException("Invalid status transition");
  }

  private void addCommentInternal(Task task, User author, String action, String message) {
    if (message == null || message.trim().isEmpty()) {
      return;
    }

    TaskComment comment = TaskComment.builder()
        .task(task)
        .author(author)
        .action(action)
        .message(message.trim())
        .build();

    taskCommentRepository.save(comment);
  }

  private String normalizeIssueUrl(String githubIssueUrl) {
    if (githubIssueUrl == null) {
      return null;
    }

    String normalized = githubIssueUrl.trim();
    if (normalized.isEmpty()) {
      return null;
    }

    String lower = normalized.toLowerCase(Locale.ROOT);
    if (lower.startsWith("http://") || lower.startsWith("https://")) {
      return normalized;
    }

    return "https://" + normalized;
  }

  private void validateCanReassign(Long actorUserId, Long projectId, Task task) {
    ProjectMember actorMember = projectAuthorizationService.getProjectMember(actorUserId, projectId);
    if (actorMember.getRole() == ProjectRole.OWNER || actorMember.getRole() == ProjectRole.ADMIN) {
      return;
    }

    TeamMember actorTeamMember = teamMemberRepository.findByUserIdAndTeamId(actorUserId, task.getAssignedTeam().getId())
        .orElseThrow(() -> new AccessDeniedException("Not a member of this team"));

    if (actorTeamMember.getRole() != TeamRole.LEAD) {
      throw new AccessDeniedException("Only team leads can reassign tasks");
    }
  }
}
