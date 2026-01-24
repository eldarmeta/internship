package com.eldarmeta.internship.service;

import com.eldarmeta.internship.dto.UserRegistrationDto;
import com.eldarmeta.internship.exception.UsernameAlreadyExistsException;
import com.eldarmeta.internship.model.User;
import com.eldarmeta.internship.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void registerUser_shouldSaveNewUser_withHashedPassword() {
        // arrange
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("eldar");
        dto.setPassword("pass123");

        when(userRepository.existsByUsername("eldar")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("HASHED");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // act
        User saved = userService.registerUser(dto);

        // assert
        assertNotNull(saved);
        assertEquals("eldar", saved.getUsername());
        assertEquals("HASHED", saved.getPasswordHash());

        // дополнительно проверяем, что реально сохраняли объект с нужными полями
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User captured = captor.getValue();
        assertEquals("eldar", captured.getUsername());
        assertEquals("HASHED", captured.getPasswordHash());
    }

    @Test
    void registerUser_shouldThrow_whenUsernameAlreadyExists() {
        // arrange
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("eldar");
        dto.setPassword("pass123");

        when(userRepository.existsByUsername("eldar")).thenReturn(true);

        // act + assert
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.registerUser(dto));

        // важно: если username занят — мы НЕ должны хэшировать и сохранять
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void findByUsername_shouldReturnOptionalFromRepository() {
        // arrange
        User u = new User();
        u.setUsername("eldar");
        u.setPasswordHash("HASHED");

        when(userRepository.findByUsername("eldar")).thenReturn(Optional.of(u));

        // act
        Optional<User> result = userService.findByUsername("eldar");

        // assert
        assertTrue(result.isPresent());
        assertEquals("eldar", result.get().getUsername());
    }
}
