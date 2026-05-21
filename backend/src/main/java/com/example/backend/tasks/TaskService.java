package com.example.backend.tasks;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.backend.projects.Project;
import com.example.backend.projects.ProjectService;
import com.example.backend.tasks.DTOs.TaskCreateRequest;
import com.example.backend.tasks.DTOs.TaskCreateResponse;
import com.example.backend.teammembers.TeamMemberService;
import com.example.backend.teams.Team;
import com.example.backend.teams.TeamService;
import com.example.backend.users.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final ProjectService projectService;
  private final TeamService teamService;
  private final TeamMemberService teamMemberService;

  public TaskCreateResponse createTask(User user, Long projectId, TaskCreateRequest request) {
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
        .assignedUser(assignedUser)
        .assignedTeam(team)
        .project(project)
        .build();

    taskRepository.save(task);
    return new TaskCreateResponse(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getPriority(),
        task.getStatus(),
        task.getDueDate(),
        task.getAssignedUser() != null ? task.getAssignedUser().getId():null,
        task.getAssignedTeam().getId(),
        task.getProject().getId());
  }
}
