package com.example.backend.users;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.auth.security.CustomUserDetails;
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
  public UserProfileResponse getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return userService.getMyProfile(userDetails.getUser());
  }

  @PatchMapping("/profile/username")
  public UserProfileResponse updateMyUsername(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody UpdateUsernameRequest request) {
    return userService.updateMyUsername(userDetails.getUser(), request);
  }

  @PatchMapping("/profile/password")
  public void changeMyPassword(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody ChangePasswordRequest request) {
    userService.changeMyPassword(userDetails.getUser(), request);
  }
}
