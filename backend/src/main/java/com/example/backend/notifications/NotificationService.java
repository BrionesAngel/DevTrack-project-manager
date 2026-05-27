package com.example.backend.notifications;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.notifications.DTOs.NotificationCreateRequest;
import com.example.backend.notifications.DTOs.NotificationResponse;
import com.example.backend.notifications.exceptions.InvalidNotificationActionException;
import com.example.backend.projectmember.ProjectMember;
import com.example.backend.projectmember.ProjectMemberRepository;
import com.example.backend.projectmember.ProjectRole;
import com.example.backend.projects.Project;
import com.example.backend.projects.ProjectRepository;
import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.teammembers.TeamMember;
import com.example.backend.teammembers.TeamMemberRepository;
import com.example.backend.teammembers.TeamRole;
import com.example.backend.teams.Team;
import com.example.backend.teams.TeamRepository;
import com.example.backend.users.User;
import com.example.backend.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final TeamRepository teamRepository;
  private final ProjectMemberRepository projectMemberRepository;
  private final TeamMemberRepository teamMemberRepository;

  public Notification getNotificationIfRecipient(Long notificationId, Long userId) {
    return notificationRepository.findById(notificationId)
        .filter(n -> n.getRecipient().getId().equals(userId))
        .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
  }

  public List<NotificationResponse> getMyNotifications(Long userId) {
    return notificationRepository.findAllByRecipientIdOrderByCreatedAtDesc(userId).stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public NotificationResponse createNotification(User sender, NotificationCreateRequest request) {
    User recipient = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    validateRequest(request);
    validateSenderAuthorization(sender, request);

    Notification notification = Notification.builder()
        .sender(sender)
        .recipient(recipient)
        .type(request.type())
        .resourceType(request.resourceType())
        .resourceId(request.resourceId())
        .createdAt(LocalDateTime.now())
        .build();
    return toResponse(notificationRepository.save(notification));
  }

  @Transactional
  public NotificationResponse acceptNotification(Long notificationId, Long userId) {
    Notification notification = getNotificationIfRecipient(notificationId, userId);

    if (notification.getStatus() != NotificationStatus.PENDING) {
      throw new InvalidNotificationActionException("Notification already processed");
    }

    switch (notification.getType()) {
      case PROJECT_INVITATION -> acceptProjectInvitation(notification, userId);
      case TEAM_INVITATION -> acceptTeamInvitation(notification, userId);
      default -> throw new InvalidNotificationActionException("Unsupported notification type: " + notification.getType());
    }

    createInvitationResponseNotification(notification, userId, true);
    notification.setStatus(NotificationStatus.ACCEPTED);
    return toResponse(notificationRepository.save(notification));
  }

  @Transactional
  public NotificationResponse rejectNotification(Long notificationId, Long userId) {
    Notification notification = getNotificationIfRecipient(notificationId, userId);

    if (notification.getStatus() != NotificationStatus.PENDING) {
      throw new InvalidNotificationActionException("Notification already processed");
    }

    createInvitationResponseNotification(notification, userId, false);
    notification.setStatus(NotificationStatus.REJECTED);
    return toResponse(notificationRepository.save(notification));
  }

  @Transactional
  public NotificationResponse markAsRead(Long notificationId, Long userId) {
    Notification notification = getNotificationIfRecipient(notificationId, userId);
    notification.setStatus(NotificationStatus.READ);
    return toResponse(notificationRepository.save(notification));
  }

  @Transactional
  public NotificationResponse createTaskAssignedNotification(User sender, User recipient, Long taskId) {
    Notification notification = Notification.builder()
        .sender(sender)
        .recipient(recipient)
        .type(NotificationType.TASK_ASSIGNED)
        .resourceType(ResourceType.TASK)
        .resourceId(taskId)
        .status(NotificationStatus.PENDING)
        .createdAt(LocalDateTime.now())
        .build();

    return toResponse(notificationRepository.save(notification));
  }

  private void validateRequest(NotificationCreateRequest request) {
    if (request.resourceType() == null) {
      throw new InvalidNotificationActionException("resourceType is required");
    }

    if (request.resourceId() == null) {
      throw new InvalidNotificationActionException("resourceId is required");
    }

    switch (request.type()) {
      case PROJECT_INVITATION -> {
        if (request.resourceType() != ResourceType.PROJECT) {
          throw new InvalidNotificationActionException("PROJECT_INVITATION must target a PROJECT resource");
        }
        ensureProjectExists(request.resourceId());
      }
      case TEAM_INVITATION -> {
        if (request.resourceType() != ResourceType.TEAM) {
          throw new InvalidNotificationActionException("TEAM_INVITATION must target a TEAM resource");
        }
        ensureTeamExists(request.resourceId());
      }
      case TASK_ASSIGNED, TASK_STATUS_CHANGED -> {
        if (request.resourceType() != ResourceType.TASK) {
          throw new InvalidNotificationActionException(request.type() + " must target a TASK resource");
        }
      }
      default -> throw new InvalidNotificationActionException("Unsupported notification type: " + request.type());
    }
  }

  private void validateSenderAuthorization(User sender, NotificationCreateRequest request) {
    switch (request.type()) {
      case PROJECT_INVITATION -> validateProjectInvitationSender(sender, request.resourceId());
      case TEAM_INVITATION -> validateTeamInvitationSender(sender, request.resourceId());
      case TASK_ASSIGNED, TASK_STATUS_CHANGED -> {
        throw new InvalidNotificationActionException("Task notifications are not supported for invitation creation");
      }
      default -> throw new InvalidNotificationActionException("Unsupported notification type: " + request.type());
    }
  }

  private void validateProjectInvitationSender(User sender, Long projectId) {
    ProjectMember member = projectMemberRepository.findByUserIdAndProjectId(sender.getId(), projectId)
        .orElseThrow(() -> new InvalidNotificationActionException("Only project owners or admins can invite to a project"));

    if (member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN) {
      throw new InvalidNotificationActionException("Only project owners or admins can invite to a project");
    }
  }

  private void validateTeamInvitationSender(User sender, Long teamId) {
    Team team = ensureTeamExists(teamId);

    boolean isProjectAdmin = projectMemberRepository.findByUserIdAndProjectId(sender.getId(), team.getProject().getId())
        .map(member -> member.getRole() == ProjectRole.OWNER || member.getRole() == ProjectRole.ADMIN)
        .orElse(false);

    boolean isTeamLead = teamMemberRepository.findByUserIdAndTeamId(sender.getId(), teamId)
        .map(member -> member.getRole() == TeamRole.LEAD)
        .orElse(false);

    if (!isProjectAdmin && !isTeamLead) {
      throw new InvalidNotificationActionException(
          "Only project owners/admins or team leads can invite to a team");
    }
  }

  private void acceptProjectInvitation(Notification notification, Long userId) {
    Project project = ensureProjectExists(notification.getResourceId());
    ensureProjectMember(userId, project);
  }

  private void acceptTeamInvitation(Notification notification, Long userId) {
    Team team = ensureTeamExists(notification.getResourceId());
    Project project = team.getProject();
    ensureProjectMember(userId, project);
    ensureTeamMember(userId, team);
  }

  private void createInvitationResponseNotification(Notification invitation, Long responderUserId, boolean accepted) {
    NotificationType responseType = switch (invitation.getType()) {
      case PROJECT_INVITATION -> accepted ? NotificationType.PROJECT_INVITATION_ACCEPTED : NotificationType.PROJECT_INVITATION_REJECTED;
      case TEAM_INVITATION -> accepted ? NotificationType.TEAM_INVITATION_ACCEPTED : NotificationType.TEAM_INVITATION_REJECTED;
      default -> throw new InvalidNotificationActionException("Unsupported notification type: " + invitation.getType());
    };

    if (invitation.getSender().getId().equals(responderUserId)) {
      return;
    }

    Notification responseNotification = Notification.builder()
        .sender(invitation.getRecipient())
        .recipient(invitation.getSender())
        .type(responseType)
        .resourceType(invitation.getResourceType())
        .resourceId(invitation.getResourceId())
        .status(NotificationStatus.READ)
        .createdAt(LocalDateTime.now())
        .build();

    notificationRepository.save(responseNotification);
  }

  private Project ensureProjectExists(Long projectId) {
    return projectRepository.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
  }

  private Team ensureTeamExists(Long teamId) {
    return teamRepository.findById(teamId)
        .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
  }

  private void ensureProjectMember(Long userId, Project project) {
    if (projectMemberRepository.existsByUserIdAndProjectId(userId, project.getId())) {
      return;
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    ProjectMember projectMember = ProjectMember.builder()
        .user(user)
        .project(project)
        .role(ProjectRole.MEMBER)
        .build();

    projectMemberRepository.save(projectMember);
  }

  private void ensureTeamMember(Long userId, Team team) {
    if (teamMemberRepository.existsByUserIdAndTeamId(userId, team.getId())) {
      return;
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    TeamMember teamMember = TeamMember.builder()
        .user(user)
        .team(team)
        .role(TeamRole.MEMBER)
        .build();

    teamMemberRepository.save(teamMember);
  }

  private NotificationResponse toResponse(Notification notification) {
    return new NotificationResponse(
        notification.getId(),
        notification.getSender().getId(),
        notification.getSender().getUsername(),
        notification.getRecipient().getId(),
        notification.getRecipient().getUsername(),
        notification.getType(),
        notification.getResourceType(),
        notification.getResourceId(),
        notification.getStatus(),
        notification.getCreatedAt());
  }
}
