package com.example.backend.notifications.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.shared.exceptions.BaseExceptionHandler;
import com.example.backend.shared.exceptions.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class NotificationExceptionHandler extends BaseExceptionHandler {

  @ExceptionHandler(InvalidNotificationActionException.class)
  public ResponseEntity<ErrorResponse> handleInvalidNotificationAction(InvalidNotificationActionException ex) {
    log.warn("Invalid notification action: {}", ex.getMessage());
    return buildError(HttpStatus.BAD_REQUEST, "INVALID_NOTIFICATION_ACTION");
  }
}