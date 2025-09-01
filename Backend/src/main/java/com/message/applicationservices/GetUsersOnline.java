package com.message.applicationservices;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;

public class GetUsersOnline {
    UserRepository repository;
    public GetUsersOnline(UserRepository repository) {
        this.repository = repository;
    }

    public Iterable<User> execute() {
        return repository.findOnlineUsers();
    }
}
