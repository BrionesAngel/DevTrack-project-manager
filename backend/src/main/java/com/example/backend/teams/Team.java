package com.example.backend.teams;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.projects.Project;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "teams")
public class Team {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)  
  @Builder.Default                                                                   
  private List<Project> projects = new ArrayList<>();
}
