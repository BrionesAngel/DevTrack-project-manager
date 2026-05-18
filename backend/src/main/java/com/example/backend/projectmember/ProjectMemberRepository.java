package com.example.backend.projectmember;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>{
  Optional<ProjectMember> findByUserIdAndProjectId(Long userId, Long projectId);
}
