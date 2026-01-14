package com.example.back.infrastructure.persistence;

import com.example.back.domain.model.User;

import java.util.List;

public final class UserEntityMapper {

    private UserEntityMapper() {
    }

    public static UserEntity toEntity(User domain) {
        return new UserEntity(domain.id(), domain.username(), domain.email());
    }

    public static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername(), entity.getEmail());
    }

    public static List<User> toDomainList(List<UserEntity> entities) {
        return entities.stream().map(UserEntityMapper::toDomain).toList();
    }
}
