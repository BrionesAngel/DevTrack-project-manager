package com.example.backend.notifications.exceptions;

public class InvalidNotificationActionException extends RuntimeException {
  public InvalidNotificationActionException(String message) {
    super(message);
  }
}