package com.example.backend.teams;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.auth.security.CustomUserDetails;
import com.example.backend.teams.DTOs.TeamCreateRequest;
import com.example.backend.teams.DTOs.TeamCreateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class TeamController {
  private final TeamService teamService;

  @PostMapping("/projects/{id}/teams")
  @ResponseStatus(HttpStatus.CREATED)
  public TeamCreateResponse createTeamOnProject(@AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody TeamCreateRequest request, @PathVariable Long id) {
    return teamService.createTeam(userDetails.getUser(), request, id);
  }

}
