package com.example.backend.teammembers;

import org.springframework.stereotype.Service;

import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.users.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamMemberService {
  private final TeamMemberRepository teamMemberRepository; 
  
  public User getUserIfIsTeamMember(Long userId, Long teamId){
    TeamMember member = teamMemberRepository.findByUserIdAndTeamId(userId, teamId)
      .orElseThrow(() -> new ResourceNotFoundException("user " + userId + "is not part of the team " + teamId));

    return member.getUser();
  }
}
