package ru.itis.dis403.lab2_06.dto;

public record RegisterRequest(
        String username,
        String password,
        String role
) {
}