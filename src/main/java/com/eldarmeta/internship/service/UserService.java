package com.eldarmeta.internship.service;

import com.eldarmeta.internship.dto.UserRegistrationDto;
import com.eldarmeta.internship.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(UserRegistrationDto userData);
    Optional<User> findByUsername(String username);
}
