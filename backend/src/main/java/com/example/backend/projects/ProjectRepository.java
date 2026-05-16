package com.example.backend.projects;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>{
  boolean existsByTitle(String title);
}
