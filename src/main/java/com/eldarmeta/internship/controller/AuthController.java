package com.eldarmeta.internship.controller;

import com.eldarmeta.internship.dto.AuthResponseDto;
import com.eldarmeta.internship.dto.LoginRequestDto;
import com.eldarmeta.internship.dto.UserRegistrationDto;
import com.eldarmeta.internship.exception.UsernameAlreadyExistsException;
import com.eldarmeta.internship.model.User;
import com.eldarmeta.internship.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRegistrationDto dto) {
        try {
            userService.registerUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponseDto("User registered successfully"));
        } catch (UsernameAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponseDto(ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto) { // <-- @Valid ДОБАВИЛИ
        User user = userService.findByUsername(dto.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDto("Invalid credentials"));
        }

        boolean ok = passwordEncoder.matches(dto.getPassword(), user.getPasswordHash());
        if (!ok) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDto("Invalid credentials"));
        }

        return ResponseEntity.ok(new AuthResponseDto("Login successful"));
    }
}
