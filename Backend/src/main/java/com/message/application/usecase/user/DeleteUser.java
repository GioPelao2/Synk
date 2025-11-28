package com.message.application.usecase.user;

import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

public class DeleteUser {
    private final UserRepository userRepository;

    public DeleteUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        userRepository.deleteById(userId);
    }
}
