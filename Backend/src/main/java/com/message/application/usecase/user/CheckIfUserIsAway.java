package com.message.application.usecase.user;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

public class CheckIfUserIsAway {
    private final UserRepository userRepository;

    public CheckIfUserIsAway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return user.isAway();
    }
}

