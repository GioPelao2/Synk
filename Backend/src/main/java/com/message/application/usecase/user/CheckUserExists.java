package com.message.application.usecase.user;

import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

public class CheckUserExists {
    private final UserRepository userRepository;

    public CheckUserExists(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return userRepository.existsById(userId);
    }
}
