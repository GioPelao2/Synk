package com.message.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;

public interface UserRepository {

    public User saveUser(User user);
    public Optional<User> findById(UserId id);
    public List<User> findOnlineUsers();

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findAvailableUsers();
}
