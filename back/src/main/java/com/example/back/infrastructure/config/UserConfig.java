package com.example.back.infrastructure.config;

import com.example.back.application.port.out.UserRepositoryPort;
import com.example.back.application.service.UserService;
import com.example.back.domain.service.UserValidationService;
import com.example.back.infrastructure.persistence.JpaUserRepositoryAdapter;
import com.example.back.infrastructure.persistence.SpringDataUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public UserValidationService userValidationService() {
        return new UserValidationService();
    }

    @Bean
    public UserRepositoryPort userRepositoryPort(SpringDataUserRepository repo) {
        return new JpaUserRepositoryAdapter(repo);
    }

    @Bean
    public UserService userService(UserRepositoryPort port, UserValidationService validationService) {
        return new UserService(port, validationService);
    }
}
