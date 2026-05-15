package com.example.backend.users.exceptions;

public class InvalidCurrentPasswordException extends RuntimeException {
  public InvalidCurrentPasswordException(String message) {
    super(message);
  }
}