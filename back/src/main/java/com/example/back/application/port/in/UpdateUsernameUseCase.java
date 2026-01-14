package com.example.back.application.port.in;

import com.example.back.domain.model.User;

public interface UpdateUsernameUseCase {
    User updateUsername(Long id, String username);
}
