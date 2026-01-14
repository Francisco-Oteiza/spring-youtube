package com.example.back.application.port.in;

import com.example.back.domain.model.User;

public interface CreateUserUseCase {
    User createUser(User user);
}
