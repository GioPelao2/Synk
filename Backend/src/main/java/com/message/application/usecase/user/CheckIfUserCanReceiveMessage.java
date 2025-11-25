package com.message.application.usecase.user;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

public class CheckIfUserCanReceiveMessage {
    private final UserRepository userRepository;

    public CheckIfUserCanReceiveMessage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean execute(UserId receiverId, UserId senderId) {
        if (receiverId == null || senderId == null) {
            throw new IllegalArgumentException("UserIds cannot be null");
        }

        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new IllegalArgumentException("Receiver not found with id: " + receiverId));

        return receiver.canReceiveMessage(senderId);
    }
}
