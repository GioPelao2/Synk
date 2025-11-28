package com.message.application.usecase.user;

import java.util.Optional;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;

public class FindUserByUsername {
    private final UserRepository userRepository;

    public FindUserByUsername(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userRepository.findByUsername(username);
    }
}
