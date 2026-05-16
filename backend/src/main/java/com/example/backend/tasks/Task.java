package com.example.backend.tasks;

import java.time.LocalDate;

import com.example.backend.teammembers.TeamMember;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
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
  @ManyToOne
  private TeamMember assignedTo;
}
