package com.example.backend.notifications;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findAllByRecipientIdOrderByCreatedAtDesc(Long userId);
}
