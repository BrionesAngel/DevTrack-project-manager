package com.example.backend.teams.DTOs;

public record TeamCreateResponse(
  Long id,
  String name,
  Long projectId
) {}
