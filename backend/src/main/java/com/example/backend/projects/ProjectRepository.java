package com.example.backend.projects;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  boolean existsByTitle(String title);

  @Query("""
    SELECT pm.project
    FROM ProjectMember pm
    WHERE pm.user.id = :memberId
    """)
  List<Project> findAllByMemberId(@Param("memberId") Long memberId);

  @Query("SELECT p FROM Project p JOIN p.members m WHERE p.id = :projectId AND m.user.id = :userId")
  Optional<Project> findByIdAndMemberId(@Param("projectId") Long projectId, @Param("userId") Long userId);

  @Query("""
      SELECT p
      FROM Project p
      JOIN ProjectMember pm
      ON pm.project = p
      WHERE p.id = :projectId
        AND pm.user.id = :userId
        AND pm.role IN ('OWNER','ADMIN')""")
  Optional<Project> findProjectIfUserHasPermission(@Param("projectId") Long projectId, @Param("userId") Long userId);
}
