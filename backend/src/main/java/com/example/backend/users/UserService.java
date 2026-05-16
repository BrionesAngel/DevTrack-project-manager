package com.example.backend.users;

import lombok.RequiredArgsConstructor;
import com.example.backend.auth.dto.RegisterRequest;
import com.example.backend.auth.exceptions.DuplicateEmailException;
import com.example.backend.users.DTOs.UserProfileResponse;
import com.example.backend.users.DTOs.ChangePasswordRequest;
import com.example.backend.users.DTOs.UpdateUsernameRequest;
import com.example.backend.users.exceptions.InvalidCurrentPasswordException;
import com.example.backend.users.exceptions.PasswordMismatchException;
import com.example.backend.users.exceptions.UsernameAlreadyExistsException;
import com.example.backend.shared.exceptions.ResourceNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User createUser(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new DuplicateEmailException("Email already exists");
    }
    User user = User.builder()
        .username(request.username())
        .password(passwordEncoder.encode(request.password()))
        .email(request.email())
        .build();

    return userRepository.save(user);
  }

  public User getUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    assert auth != null;
    String email = auth.getName();

    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }
    return user;
  }

  public UserProfileResponse getMyProfile(String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }

    return new UserProfileResponse(user.getId(), user.getUsername(), user.getEmail());
  }

  public UserProfileResponse updateMyUsername(String email, UpdateUsernameRequest request) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }

    String nextUsername = request.username().trim();
    if (!nextUsername.equals(user.getUsername()) && userRepository.existsByUsername(nextUsername)) {
      throw new UsernameAlreadyExistsException("Username already exists");
    }

    user.setUsername(nextUsername);
    userRepository.save(user);

    return new UserProfileResponse(user.getId(), user.getUsername(), user.getEmail());
  }

  public void changeMyPassword(String email, ChangePasswordRequest request) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }

    if (!request.newPassword().equals(request.confirmNewPassword())) {
      throw new PasswordMismatchException("Passwords do not match");
    }

    if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
      throw new InvalidCurrentPasswordException("Current password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(request.newPassword()));
    userRepository.save(user);
  }
}
