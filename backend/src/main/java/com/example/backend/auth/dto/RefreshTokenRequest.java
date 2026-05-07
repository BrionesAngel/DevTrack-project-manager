package com.example.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "refreshToken required") String refreshToken) {
}
