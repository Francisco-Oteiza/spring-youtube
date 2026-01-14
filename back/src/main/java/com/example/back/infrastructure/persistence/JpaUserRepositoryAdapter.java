package com.example.back.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.example.back.application.port.out.UserRepositoryPort;
import com.example.back.domain.exception.UserNotFoundException;
import com.example.back.domain.model.User;

public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springDataUserRepository;

    public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return springDataUserRepository.findById(id).map(UserEntityMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return UserEntityMapper.toDomainList(springDataUserRepository.findAll());
    }

    @Override
    public User save(User user) {
        UserEntity saved = springDataUserRepository.save(UserEntityMapper.toEntity(user));
        return UserEntityMapper.toDomain(saved);
    }

    @Override
    public User updateUsername(Long id, String username) {
        UserEntity entity = springDataUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        entity.setUsername(username);
        return UserEntityMapper.toDomain(springDataUserRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        springDataUserRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return springDataUserRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

}
