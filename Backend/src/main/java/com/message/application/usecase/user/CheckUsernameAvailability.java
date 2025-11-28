package com.message.application.usecase.user;

import com.message.domain.repositories.UserRepository;

public class CheckUsernameAvailability {
    private final UserRepository userRepository;

    public CheckUsernameAvailability(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean execute(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return !userRepository.existsByUsername(username);
    }
}

