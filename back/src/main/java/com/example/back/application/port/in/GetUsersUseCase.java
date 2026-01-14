package com.example.back.application.port.in;

import java.util.List;

import com.example.back.domain.model.User;

public interface GetUsersUseCase {
    List<User> findAll();
}
