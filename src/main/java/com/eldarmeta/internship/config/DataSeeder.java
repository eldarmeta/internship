package com.eldarmeta.internship.config;

import com.eldarmeta.internship.model.User;
import com.eldarmeta.internship.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByUsername("eldar")) {
                userRepository.save(new User("eldar", "demo_hash_for_week2"));
            }
        };
    }
}
