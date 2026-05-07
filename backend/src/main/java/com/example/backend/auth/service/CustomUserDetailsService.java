package com.example.backend.auth.service;

import lombok.RequiredArgsConstructor;
import com.example.backend.users.exceptions.UserNotFoundException;
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
  public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UserNotFoundException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new UserNotFoundException("Incorrect Email");
    }

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        java.util.List.of());
  }
}
