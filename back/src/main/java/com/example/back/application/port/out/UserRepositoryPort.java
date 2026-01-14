package com.example.back.application.port.out;

import java.util.List;
import java.util.Optional;

import com.example.back.domain.model.User;

public interface UserRepositoryPort {

    Optional<User> findById(Long id);

    List<User> findAll();

    User save(User user);

    User updateUsername(Long id, String username);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByEmail(String email);
}
