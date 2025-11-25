package com.message.application.usecase.user;

import java.util.Optional;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;

public class FindUserByEmail {
    private final UserRepository userRepository;

    public FindUserByEmail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return userRepository.findByEmail(email);
    }
}

