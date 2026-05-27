package com.example.backend.tasks;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long>{
	Optional<Task> findByIdAndProjectId(Long id, Long projectId);

	List<Task> findAllByProjectIdOrderByIdAsc(Long projectId);

	List<Task> findAllByProjectIdAndAssignedTeamIdOrderByIdAsc(Long projectId, Long teamId);

	List<Task> findAllByProjectIdAndAssignedUserIdOrderByIdAsc(Long projectId, Long userId);

	List<Task> findAllByProjectIdAndAssignedTeamIdAndAssignedUserIdOrderByIdAsc(Long projectId, Long teamId, Long userId);

	@Modifying
	@Query("update Task t set t.assignedTeam = null where t.assignedTeam.id = :teamId")
	int clearAssignedTeamByTeamId(Long teamId);
}
