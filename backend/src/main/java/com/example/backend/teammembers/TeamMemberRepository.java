package com.example.backend.teammembers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
  Optional<TeamMember> findByUserIdAndTeamId(Long userId, Long teamId);
}
