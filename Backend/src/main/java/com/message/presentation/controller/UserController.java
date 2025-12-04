package com.message.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.message.application.usecase.user.*;
import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;
import com.message.presentation.dto.UserDTO;
import com.message.presentation.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUser createUser;
    private final GetUserById getUserById;
    private final FindUserByUsername findUserByUsername;
    private final GetOnlineUsers getOnlineUsers;
    private final GetAllUsers getAllUsers;
    private final SetUserOnline setUserOnline;
    private final SetUserOffline setUserOffline;
    private final SetUserAway setUserAway;
    private final CheckUsernameAvailability checkUsernameAvailability;
    private final CheckEmailAvailability checkEmailAvailability;
    private final DeleteUser deleteUser;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserMapper userMapper;

    @Autowired
    public UserController(
            CreateUser createUser,
            GetUserById getUserById,
            FindUserByUsername findUserByUsername,
            GetOnlineUsers getOnlineUsers,
            GetAllUsers getAllUsers,
            SetUserOnline setUserOnline,
            SetUserOffline setUserOffline,
            SetUserAway setUserAway,
            CheckUsernameAvailability checkUsernameAvailability,
            CheckEmailAvailability checkEmailAvailability,
            DeleteUser deleteUser,
            UserMapper userMapper) {
        this.createUser = createUser;
        this.getUserById = getUserById;
        this.findUserByUsername = findUserByUsername;
        this.getOnlineUsers = getOnlineUsers;
        this.getAllUsers = getAllUsers;
        this.setUserOnline = setUserOnline;
        this.setUserOffline = setUserOffline;
        this.setUserAway = setUserAway;
        this.checkUsernameAvailability = checkUsernameAvailability;
        this.checkEmailAvailability = checkEmailAvailability;
        this.deleteUser = deleteUser;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = getUserById.execute(UserId.from(id));
        
        if (user.isPresent()) {
            User u = user.get();
            UserDTO dto = UserDTO.forResponse(
                u.getId().value(),
                u.getUsername(),
                u.getEmail(),
                u.getStatus(),
                u.getLastSeen()
            );
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        Optional<User> user = findUserByUsername.execute(username);

        if (user.isPresent()) {
            User u = user.get();
            UserDTO dto = UserDTO.forResponse(
                u.getId().value(),
                u.getUsername(),
                u.getEmail(),
                u.getStatus(),
                u.getLastSeen()
            );
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = getAllUsers.execute();
        List<UserDTO> dtos = users.stream()
            .map(user -> UserDTO.forResponse(
                user.getId().value(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getLastSeen()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/online")
    public ResponseEntity<List<UserDTO>> getOnlineUsers() {
        List<User> users = getOnlineUsers.execute();
        List<UserDTO> dtos = users.stream()
            .map(user -> UserDTO.forResponse(
                user.getId().value(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getLastSeen()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        logger.info("Iniciando registro de nuevo usuario: {}", userDTO.getUsername());
        try {
            if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (userDTO.getPassword() == null || userDTO.getPassword().length() < 8) {
                return ResponseEntity.badRequest().body("Password must be at least 8 characters");
            }

            User savedUser = createUser.execute(
                userDTO.getUsername(), 
                userDTO.getEmail(),
                userDTO.getPassword()
            );

            logger.info("Usuario registrado exitosamente. ID: {}", savedUser.getId().value());

            UserDTO response = UserDTO.forResponse(
                savedUser.getId().value(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getStatus(),
                savedUser.getLastSeen()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Fallo en registro de usuario (datos inválidos o duplicados): {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam String username) {
        try {
            boolean available = checkUsernameAvailability.execute(username);
            return ResponseEntity.ok(available);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        try {
            boolean available = checkEmailAvailability.execute(email);
            return ResponseEntity.ok(available);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/online")
    public ResponseEntity<UserDTO> setUserOnline(@PathVariable Long id) {
        try {
            setUserOnline.execute(UserId.from(id));

            Optional<User> userOpt = getUserById.execute(UserId.from(id));
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOpt.get();
            UserDTO response = UserDTO.forResponse(
                user.getId().value(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getLastSeen()
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/offline")
    public ResponseEntity<UserDTO> setUserOffline(@PathVariable Long id) {
        try {
            setUserOffline.execute(UserId.from(id));

            Optional<User> userOpt = getUserById.execute(UserId.from(id));
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOpt.get();
            UserDTO response = UserDTO.forResponse(
                user.getId().value(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getLastSeen()
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/away")
    public ResponseEntity<UserDTO> setUserAway(@PathVariable Long id) {
        try {
            setUserAway.execute(UserId.from(id));

            Optional<User> userOpt = getUserById.execute(UserId.from(id));
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOpt.get();
            UserDTO response = UserDTO.forResponse(
                user.getId().value(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getLastSeen()
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            deleteUser.execute(UserId.from(id));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
