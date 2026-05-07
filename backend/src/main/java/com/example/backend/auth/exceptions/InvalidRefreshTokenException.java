package com.example.backend.auth.exceptions;

public class InvalidRefreshTokenException extends RuntimeException {
  public InvalidRefreshTokenException(String message) {
    super(message);
  }
}
