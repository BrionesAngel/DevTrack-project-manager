package com.example.backend.teammembers;

import com.example.backend.teams.Team;
import com.example.backend.users.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "teammembers")
public class TeamMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private User user;
  @ManyToOne
  private Team team;
  @Enumerated(EnumType.STRING)
  private TeamRole role;
}
