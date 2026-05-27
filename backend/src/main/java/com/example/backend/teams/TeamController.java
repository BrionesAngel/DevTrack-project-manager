package com.example.backend.teams;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.auth.security.CustomUserDetails;
import com.example.backend.teams.DTOs.TeamCreateRequest;
import com.example.backend.teams.DTOs.TeamCreateResponse;
import com.example.backend.teams.DTOs.TeamDetailsResponse;
import com.example.backend.teammembers.DTOs.TeamMemberOverview;
import com.example.backend.teammembers.DTOs.UpdateTeamMemberRoleRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class TeamController {
  private final TeamService teamService;

  @PostMapping("/projects/{id}/teams")
  @ResponseStatus(HttpStatus.CREATED)
  public TeamCreateResponse createTeamOnProject(@AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody TeamCreateRequest request, @PathVariable Long id) {
    return teamService.createTeam(userDetails.getUser(), request, id);
  }

  @GetMapping("/projects/{projectId}/teams/{teamId}")
  public TeamDetailsResponse getTeamDetails(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId, @PathVariable Long teamId) {
    return teamService.getTeamDetails(userDetails.getUser().getId(), projectId, teamId);
  }

  @PatchMapping("/projects/{projectId}/teams/{teamId}/members/{userId}/role")
  public TeamMemberOverview updateTeamMemberRole(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long projectId, @PathVariable Long teamId, @PathVariable Long userId,
      @Valid @RequestBody UpdateTeamMemberRoleRequest request) {
    return teamService.upsertTeamMemberRole(userDetails.getUser().getId(), projectId, teamId, userId, request);
  }

  @DeleteMapping("/projects/{projectId}/teams/{teamId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTeamOnProject(@AuthenticationPrincipal CustomUserDetails userDetails,
    @PathVariable Long projectId, @PathVariable Long teamId) {
    teamService.deleteTeam(userDetails.getUser().getId(),projectId, teamId);
  }

}
