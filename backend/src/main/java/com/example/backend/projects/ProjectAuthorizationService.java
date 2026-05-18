package com.example.backend.projects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.backend.projectmember.ProjectMember;
import com.example.backend.projectmember.ProjectMemberRepository;
import com.example.backend.projectmember.ProjectRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProjectAuthorizationService {
  private final ProjectMemberRepository projectMemberRepository;

  public ProjectMember getProjectMember(Long userId, Long projectId) {
    return projectMemberRepository
        .findByUserIdAndProjectId(userId, projectId)
        .orElseThrow(() -> new AccessDeniedException("Not a member of this project"));
  }

  public void validateOwner(Long userId, Long projectId) {
    ProjectMember member = getProjectMember(userId, projectId);

    if (member.getRole() != ProjectRole.OWNER) {
      throw new AccessDeniedException("not project owner");
    }
  }

  public void validateAdmin(Long userId, Long projectId) {
    ProjectMember member = getProjectMember(userId, projectId);

    if (member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN) {
      throw new AccessDeniedException("not project admin");
    }
  }
}
