package com.example.backend.users;

import lombok.RequiredArgsConstructor;
import com.example.backend.auth.dto.RegisterRequest;
import com.example.backend.auth.exceptions.DuplicateEmailException;
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
}
