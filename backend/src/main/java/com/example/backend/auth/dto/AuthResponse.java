package com.example.backend.auth.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken) {
}
