package com.message.application.usecase;

import java.util.Optional;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

public class GetUserById {
    private UserRepository repository;

    public Optional<User> execute(UserId userId) {
        return repository.findById(userId);
    }
}
