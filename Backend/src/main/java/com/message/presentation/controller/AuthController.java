package com.message.presentation.controller;

import com.message.application.usecase.user.FindUserByUsername;
import com.message.domain.entities.User;
import com.message.presentation.dto.LoginRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController 
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final FindUserByUsername findUserByUsername;

    @Autowired
    public AuthController(FindUserByUsername findUserByUsername) {
        this.findUserByUsername = findUserByUsername;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Intento de inicio de sesión - Usuario: {}", loginRequest.getUsername());
        
        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("{\"message\": \"Username is required\"}");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("{\"message\": \"Password is required\"}");
            }

            Optional<User> userOpt = findUserByUsername.execute(loginRequest.getUsername());
            
            if (userOpt.isEmpty()) {
                logger.warn("Login FALLIDO - Usuario no encontrado: {}", loginRequest.getUsername());
                return ResponseEntity.status(401).body("{\"message\": \"Credenciales inválidas\"}");
            }

            User user = userOpt.get();
            
            if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPasswordHash())) {
                logger.warn("Login FALLIDO - Contraseña incorrecta para usuario: {}", loginRequest.getUsername());
                return ResponseEntity.status(401).body("{\"message\": \"Credenciales inválidas\"}");
            }

            logger.info("Login EXITOSO para usuario: {}", loginRequest.getUsername());
            return ResponseEntity.ok(String.format(
                "{\"token\": \"fake-jwt-token-123\", \"message\": \"Login Exitoso\", \"userId\": %d, \"username\": \"%s\"}", 
                user.getId().value(),
                user.getUsername()
            ));
            
        } catch (Exception e) {
            logger.error("Error durante el login: {}", e.getMessage());
            return ResponseEntity.status(500).body("{\"message\": \"Error interno del servidor\"}");
        }
    }
}
