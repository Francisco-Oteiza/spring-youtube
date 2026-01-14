package com.example.back.application.service;

import com.example.back.application.port.out.UserRepositoryPort;
import com.example.back.domain.exception.UserNotFoundException;
import com.example.back.domain.model.User;
import com.example.back.domain.service.UserValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort repo;

    @Spy
    private UserValidationService validation = new UserValidationService();

    @InjectMocks
    private UserService service;

    @Test
    void createUser_saves_and_returns_user_with_id() {
        User input = new User(null, "john", "john@demo.com");

        when(repo.existsByEmail("john@demo.com")).thenReturn(false);
        when(repo.save(any(User.class))).thenReturn(new User(1L, "john", "john@demo.com"));

        User created = service.createUser(input);

        assertEquals(1L, created.id());
        assertEquals("john", created.username());
        assertEquals("john@demo.com", created.email());

        verify(repo).existsByEmail("john@demo.com");
        verify(repo).save(any(User.class));
        verifyNoMoreInteractions(repo);
    }

    @Test
    void deleteById_when_missing_throws_UserNotFoundException() {
        when(repo.existsById(999L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> service.deleteById(999L));

        verify(repo).existsById(999L);
        verify(repo, never()).deleteById(anyLong());
        verifyNoMoreInteractions(repo);
    }

    @Test
    void deleteById_when_present_deletes() {
        when(repo.existsById(2L)).thenReturn(true);

        service.deleteById(2L);

        verify(repo).existsById(2L);
        verify(repo).deleteById(2L);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void updateUsername_updates_and_saves() {
        when(repo.existsById(5L)).thenReturn(true);

        when(repo.updateUsername(5L, "newname"))
                .thenReturn(new User(5L, "newname", "a@demo.com"));

        User updated = service.updateUsername(5L, "newname");

        assertEquals(5L, updated.id());
        assertEquals("newname", updated.username());
        assertEquals("a@demo.com", updated.email());

        verify(repo).existsById(5L);
        verify(repo).updateUsername(5L, "newname");
        verifyNoMoreInteractions(repo);
    }

    void updateUsername_when_missing_throws() {
        when(repo.existsById(123L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> service.updateUsername(123L, "x"));

        verify(repo).existsById(123L);
        verify(repo, never()).updateUsername(anyLong(), anyString());
        verifyNoMoreInteractions(repo);
    }
}
