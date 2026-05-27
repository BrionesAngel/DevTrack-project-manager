package com.example.backend.projectmember;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>{
  Optional<ProjectMember> findByUserIdAndProjectId(Long userId, Long projectId);

  Optional<ProjectMember> findFirstByProjectIdAndRole(Long projectId, ProjectRole role);

  List<ProjectMember> findAllByProjectId(Long projectId);

  List<ProjectMember> findAllByProjectIdAndRole(Long projectId, ProjectRole role);

  long countByProjectIdAndRole(Long projectId, ProjectRole role);

  boolean existsByUserIdAndProjectId(Long userId, Long projectId);
}
