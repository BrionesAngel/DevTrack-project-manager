package com.example.backend.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.shared.exceptions.BaseExceptionHandler;
import com.example.backend.shared.exceptions.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class UsersExceptionHandler extends BaseExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
    log.warn("User not found: {}", ex.getMessage());
    return buildError(HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
  }
}
