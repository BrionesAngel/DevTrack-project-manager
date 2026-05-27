package com.example.backend.notifications;

import com.example.backend.users.User;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private User recipient;
  @ManyToOne
  private User sender;

  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Enumerated(EnumType.STRING)
  private ResourceType resourceType;

  private Long resourceId;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private NotificationStatus status = NotificationStatus.PENDING;

  private LocalDateTime createdAt;
}
