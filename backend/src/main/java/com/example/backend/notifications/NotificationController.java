package com.example.backend.notifications;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.auth.security.CustomUserDetails;
import com.example.backend.notifications.DTOs.NotificationCreateRequest;
import com.example.backend.notifications.DTOs.NotificationResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationService notificationService;

  @GetMapping
  public List<NotificationResponse> getMyNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return notificationService.getMyNotifications(userDetails.getUser().getId());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public NotificationResponse createNotification(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody NotificationCreateRequest request) {
    return notificationService.createNotification(userDetails.getUser(), request);
  }

  @PatchMapping("/{id}/accept")
  public NotificationResponse acceptNotification(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id) {
    return notificationService.acceptNotification(id, userDetails.getUser().getId());
  }

  @PatchMapping("/{id}/reject")
  public NotificationResponse rejectNotification(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id) {
    return notificationService.rejectNotification(id, userDetails.getUser().getId());
  }

  @PatchMapping("/{id}/read")
  public NotificationResponse markNotificationAsRead(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long id) {
    return notificationService.markAsRead(id, userDetails.getUser().getId());
  }
}