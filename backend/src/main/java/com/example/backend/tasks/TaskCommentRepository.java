package com.example.backend.tasks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
  List<TaskComment> findAllByTaskIdOrderByCreatedAtAsc(Long taskId);
}
