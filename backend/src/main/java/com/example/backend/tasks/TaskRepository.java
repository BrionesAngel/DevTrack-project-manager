package com.example.backend.tasks;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>{
	Optional<Task> findByIdAndProjectId(Long id, Long projectId);

	List<Task> findAllByProjectIdOrderByIdAsc(Long projectId);

	List<Task> findAllByProjectIdAndAssignedTeamIdOrderByIdAsc(Long projectId, Long teamId);

	List<Task> findAllByProjectIdAndAssignedUserIdOrderByIdAsc(Long projectId, Long userId);

	List<Task> findAllByProjectIdAndAssignedTeamIdAndAssignedUserIdOrderByIdAsc(Long projectId, Long teamId, Long userId);
}
