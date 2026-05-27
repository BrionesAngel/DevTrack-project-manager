package com.example.backend.teammembers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
  Optional<TeamMember> findByUserIdAndTeamId(Long userId, Long teamId);

  List<TeamMember> findAllByTeamId(Long teamId);

  long countByTeamIdAndRole(Long teamId, TeamRole role);

  boolean existsByUserIdAndTeamId(Long userId, Long teamId);
}
