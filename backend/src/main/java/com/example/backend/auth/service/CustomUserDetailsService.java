package com.example.backend.auth.service;

import lombok.RequiredArgsConstructor;

import com.example.backend.auth.security.CustomUserDetails;
import com.example.backend.shared.exceptions.ResourceNotFoundException;
import com.example.backend.users.User;
import com.example.backend.users.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public @NonNull UserDetails loadUserByUsername(@NonNull String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    return new CustomUserDetails(user);
  }
}
