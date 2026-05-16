package com.example.backend.users;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.users.DTOs.ChangePasswordRequest;
import com.example.backend.users.DTOs.UserProfileResponse;
import com.example.backend.users.DTOs.UpdateUsernameRequest;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/profile")
  public UserProfileResponse getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
    return userService.getMyProfile(userDetails.getUsername());
  }

  @PatchMapping("/profile/username")
  public UserProfileResponse updateMyUsername(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody UpdateUsernameRequest request) {
    return userService.updateMyUsername(userDetails.getUsername(), request);
  }

  @PatchMapping("/profile/password")
  public void changeMyPassword(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody ChangePasswordRequest request) {
    userService.changeMyPassword(userDetails.getUsername(), request);
  }
}
