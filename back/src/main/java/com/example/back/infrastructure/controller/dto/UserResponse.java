package com.example.back.infrastructure.controller.dto;

import com.example.back.domain.model.User;

public record UserResponse(Long id, String username, String email) {
        public static UserResponse from(User user) {
                return new UserResponse(user.id(), user.username(), user.email());
        }
}
