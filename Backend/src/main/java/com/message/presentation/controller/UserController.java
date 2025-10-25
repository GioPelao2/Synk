package com.message.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.UserRepositoryImpl;
import com.message.presentation.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays; 
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepositoryImpl userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(UserId.from(id));

        if (user.isPresent()) {
            User u = user.get();
            UserDTO dto = UserDTO.forResponse(u.getId().value(), u.getUsername(),
                                            u.getEmail(), u.getStatus(), u.getLastSeen());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar usuario por username
    @GetMapping("/search")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            User u = user.get();
            UserDTO dto = UserDTO.forResponse(u.getId().value(), u.getUsername(),
                                            u.getEmail(), u.getStatus(), u.getLastSeen());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    // Obtener usuarios online
    @GetMapping("/online")
    public ResponseEntity<List<UserDTO>> getOnlineUsers() {
       UserDTO mockUser1 = UserDTO.forResponse(
        1L, 
        "AdminMock", 
        "admin@mock.com", 
        UserStatus.ONLINE, // Usar el ENUM correcto
        null
    );
    UserDTO mockUser2 = UserDTO.forResponse(
        2L, 
        "TestUser", 
        "test@mock.com", 
        UserStatus.OFFLINE, 
        LocalDateTime.now()
    );
        
        List<UserDTO> dtos = Arrays.asList(mockUser1, mockUser2);
        /* 
        List<User> users = userRepository.findOnlineUsers();
        List<UserDTO> dtos = users.stream()
            .map(user -> UserDTO.forResponse(user.getId().value(), user.getUsername(),
                                           user.getEmail(), user.getStatus(), user.getLastSeen()))
            .collect(Collectors.toList());
            */
            System.out.println("✅ Devolviendo MOCK de usuarios online.");
        return ResponseEntity.ok(dtos);
    }

    // Registrar nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        // Validación básica
        if (userDTO.getUsername() == null || userDTO.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (userRepository.existsByUsername(userDTO.getUsername()) ||
            userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User newUser = new User(userDTO.getUsername(), userDTO.getEmail());
        User savedUser = userRepository.saveUser(newUser);

        UserDTO response = UserDTO.forResponse(savedUser.getId().value(), savedUser.getUsername(),
                                              savedUser.getEmail(), savedUser.getStatus(), savedUser.getLastSeen());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/online")
    public ResponseEntity<UserDTO> setUserOnline(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(UserId.from(id));

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        user.goOnline();
        User updatedUser = userRepository.saveUser(user);

        UserDTO response = UserDTO.forResponse(updatedUser.getId().value(), updatedUser.getUsername(),
                                              updatedUser.getEmail(), updatedUser.getStatus(), updatedUser.getLastSeen());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/offline")
    public ResponseEntity<UserDTO> setUserOffline(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(UserId.from(id));

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        user.goOffline();
        User updatedUser = userRepository.saveUser(user);

        UserDTO response = UserDTO.forResponse(updatedUser.getId().value(), updatedUser.getUsername(),
                                              updatedUser.getEmail(), updatedUser.getStatus(), updatedUser.getLastSeen());
        return ResponseEntity.ok(response);
    }
}
