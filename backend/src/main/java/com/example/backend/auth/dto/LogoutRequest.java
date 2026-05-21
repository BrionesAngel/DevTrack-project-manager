package com.example.backend.auth.dto;

public record LogoutRequest(
    String refreshToken) {
}
