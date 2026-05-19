package com.example.backend.projects;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.projectmember.ProjectMember;
import com.example.backend.tasks.Task;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by")
  private User createdBy;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Team> teams = new ArrayList<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ProjectMember> members =  new ArrayList<>();
  
}
