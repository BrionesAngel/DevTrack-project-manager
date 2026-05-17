package com.example.backend.tasks;

import java.time.LocalDate;

import com.example.backend.projects.Project;
import com.example.backend.teams.Team;
import com.example.backend.users.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tasks")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;

  @Enumerated(EnumType.STRING)
  private TaskPriority TaskPriority;

  @Enumerated(EnumType.STRING)
  private TaskStatus taskStatus;

  private LocalDate dueDate;

  @ManyToOne(fetch = FetchType.LAZY)
  private User assignedTo;

  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  private Team team;

}
