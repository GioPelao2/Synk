package com.message.application.usecase.user;

import java.time.LocalDateTime;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

public class GetUserLastSeen {
    private final UserRepository userRepository;

    public GetUserLastSeen(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LocalDateTime execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return user.getLastSeen();
    }
}
