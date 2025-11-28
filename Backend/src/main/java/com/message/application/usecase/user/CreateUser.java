package com.message.application.usecase.user;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

/*
 * basado en constructor User(String username, String email)
 */
public class CreateUser {
    private final UserRepository userRepository;

    public CreateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

public User execute(String username, String email, String password) {
    if (username == null || username.trim().isEmpty()) {
        throw new IllegalArgumentException("Username cannot be null or empty");
    }
    if (email == null || email.trim().isEmpty()) {
        throw new IllegalArgumentException("Email cannot be null or empty");
    }
    if (password == null || password.length() < 8) {
        throw new IllegalArgumentException("Password must be at least 8 characters");
    }

    if (userRepository.existsByUsername(username)) {
        throw new IllegalArgumentException("Username already exists: " + username);
    }
    if (userRepository.existsByEmail(email)) {
        throw new IllegalArgumentException("Email already exists: " + email);
    }

    String passwordHash = hashPassword(password);
    User newUser = new User(username, email, passwordHash);
    return userRepository.saveUser(newUser);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
