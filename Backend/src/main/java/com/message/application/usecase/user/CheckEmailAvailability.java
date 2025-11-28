package com.message.application.usecase.user;

import com.message.domain.repositories.UserRepository;

public class CheckEmailAvailability {
    private final UserRepository userRepository;

    public CheckEmailAvailability(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean execute(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return !userRepository.existsByEmail(email);
    }
}
