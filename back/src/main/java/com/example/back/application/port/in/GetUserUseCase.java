package com.example.back.application.port.in;

import java.util.Optional;

import com.example.back.domain.model.User;

public interface GetUserUseCase {
    Optional<User> findById(Long id);
}
