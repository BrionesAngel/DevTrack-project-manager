package com.example.backend.users.DTOs;

public record UserProfileResponse(
    Long id,
    String userName,
    String email) {
}
