package com.example.backend.teams;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
 
  Optional<Team> findByIdAndProjectId(Long teamId, Long projectId);
}
