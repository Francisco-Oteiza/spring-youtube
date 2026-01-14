package com.example.back.application.service;

import java.util.List;
import java.util.Optional;

import com.example.back.application.port.in.CreateUserUseCase;
import com.example.back.application.port.in.DeleteUserUseCase;
import com.example.back.application.port.in.GetUserUseCase;
import com.example.back.application.port.in.GetUsersUseCase;
import com.example.back.application.port.in.UpdateUsernameUseCase;
import com.example.back.application.port.out.UserRepositoryPort;
import com.example.back.domain.exception.EmailAlreadyInUseException;
import com.example.back.domain.exception.UserNotFoundException;
import com.example.back.domain.model.User;
import com.example.back.domain.service.UserValidationService;

public class UserService implements
        CreateUserUseCase,
        GetUserUseCase,
        GetUsersUseCase,
        DeleteUserUseCase,
        UpdateUsernameUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserValidationService userValidationService;

    public UserService(UserRepositoryPort userRepositoryPort, UserValidationService userValidationService) {
        this.userRepositoryPort = userRepositoryPort;
        this.userValidationService = userValidationService;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepositoryPort.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User createUser(User user) {
        userValidationService.validateEmail(user.email());

        if (userRepositoryPort.existsByEmail(user.email())) {
            throw new EmailAlreadyInUseException(user.email());
        }

        return userRepositoryPort.save(user);
    }

    @Override
    public User updateUsername(Long id, String username) {
        if (!userRepositoryPort.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        String newName = username == null ? "" : username.trim();
        if (newName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        return userRepositoryPort.updateUsername(id, newName);
    }

    // @Override
    // public User updateUsername(Long id, String username) {
    // if (!userRepositoryPort.existsById(id)) {
    // throw new UserNotFoundException(id);
    // }

    // User current = userRepositoryPort.findById(id)
    // .orElseThrow(() -> new UserNotFoundException(id));

    // User updated = new User(current.id(), username, current.email());

    // return userRepositoryPort.save(updated);
    // }

    @Override
    public void deleteById(Long id) {
        if (!userRepositoryPort.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepositoryPort.deleteById(id);
    }

}
