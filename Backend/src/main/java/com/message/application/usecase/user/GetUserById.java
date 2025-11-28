package com.message.application.usecase.user;

import java.util.Optional;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

public class GetUserById {
    private final UserRepository userRepository;

    public GetUserById(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return userRepository.findById(userId);
    }
}
