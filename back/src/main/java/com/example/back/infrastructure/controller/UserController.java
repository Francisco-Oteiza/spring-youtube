package com.example.back.infrastructure.controller;

import com.example.back.application.port.in.CreateUserUseCase;
import com.example.back.application.port.in.DeleteUserUseCase;
import com.example.back.application.port.in.GetUserUseCase;
import com.example.back.application.port.in.GetUsersUseCase;
import com.example.back.application.port.in.UpdateUsernameUseCase;
import com.example.back.domain.exception.UserNotFoundException;
import com.example.back.domain.model.User;
import com.example.back.infrastructure.controller.dto.UpdateUsernameRequest;
import com.example.back.infrastructure.controller.dto.UserRequest;
import com.example.back.infrastructure.controller.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdateUsernameUseCase updateUsernameUseCase;

    public UserController(
            CreateUserUseCase createUserUseCase,
            GetUserUseCase getUserUseCase,
            GetUsersUseCase getUsersUseCase,
            DeleteUserUseCase deleteUserUseCase,
            UpdateUsernameUseCase updateUsernameUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.getUsersUseCase = getUsersUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.updateUsernameUseCase = updateUsernameUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        User user = getUserUseCase.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> result = getUsersUseCase.findAll().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest req) {
        User created = createUserUseCase.createUser(new User(null, req.username(), req.email()));

        return ResponseEntity
                .created(URI.create("/api/users/" + created.id()))
                .body(UserResponse.from(created));
    }

    @PatchMapping("/{id}/username")
    public ResponseEntity<UserResponse> updateUsername(
            @PathVariable Long id,
            @RequestBody UpdateUsernameRequest req) {
        User updated = updateUsernameUseCase.updateUsername(id, req.username());
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
