package com.example.backend.projects;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.tasks.Task;
import com.example.backend.users.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "projects")
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;
  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true) 
  @Builder.Default
  private List<Task> tasks = new ArrayList<>();
  @JoinColumn(name = "created_by")
  private User createdBy;
}
