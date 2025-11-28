package com.message.application.usecase.user;

import java.util.List;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;

public class GetOnlineUsers {
    private final UserRepository userRepository;

    public GetOnlineUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> execute() {
        return userRepository.findOnlineUsers();
    }
}
