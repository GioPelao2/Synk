package com.message.application.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(UserId id) {
        return userRepository.findById(id);
    }

    public User createUser(String username, String email) {
        // Verificar que el username y email no existan
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        User newUser = new User(username, email);
        return userRepository.saveUser(newUser);
    }

    public void updateUserStatus(UserId userId, UserStatus status) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        User user = userOpt.get();
        user.changeStatus(status);
        userRepository.saveUser(user);
    }

    public void setUserOnline(UserId userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        User user = userOpt.get();
        user.goOnline();
        userRepository.saveUser(user);
    }

    public void setUserOffline(UserId userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        User user = userOpt.get();
        user.goOffline();
        userRepository.saveUser(user);
    }

    public void setUserAway(UserId userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        User user = userOpt.get();
        user.goAway();
        userRepository.saveUser(user);
    }

    public List<User> getOnlineUsers() {
        return userRepository.findOnlineUsers();
    }

    // Obtener usuarios disponibles (ONLINE y AWAY)
    public List<User> getAvailableUsers() {
        return userRepository.findAvailableUsers();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Verificar si un usuario puede recibir mensajes
    public boolean canReceiveMessage(UserId receiverId, UserId senderId) {
        Optional<User> receiverOpt = userRepository.findById(receiverId);
        if (receiverOpt.isEmpty()) {
            return false;
        }

        User receiver = receiverOpt.get();
        return receiver.canReceiveMessage(senderId);
    }

    public boolean userExists(UserId userId) {
        return userRepository.findById(userId).isPresent();
    }

    // Verificar disponibilidad de username
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    // Verificar disponibilidad de email
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    // Actualizar información del usuario
    public User updateUser(UserId userId, String newUsername, String newEmail) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        // Verificar que el nuevo username no esté en uso (si cambió)
        User currentUser = userOpt.get();
        if (!currentUser.getUsername().equals(newUsername) &&
            userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already exists: " + newUsername);
        }

        // Verificar que el nuevo email no esté en uso (si cambió)
        if (!currentUser.getEmail().equals(newEmail) &&
            userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already exists: " + newEmail);
        }

        // Como User es inmutable, hay que crear uno nuevo o
        // añadir métodos de actualización a la entidad User
        // Por ahora

        return userRepository.saveUser(currentUser);
    }
}
