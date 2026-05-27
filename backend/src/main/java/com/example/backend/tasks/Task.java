package com.example.backend.tasks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
  private TaskPriority priority;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  private LocalDate dueDate;

  private String githubIssueUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  private User assignedUser;

  @ManyToOne(fetch = FetchType.LAZY)
  private Team assignedTeam;

  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TaskComment> comments = new ArrayList<>();

}
