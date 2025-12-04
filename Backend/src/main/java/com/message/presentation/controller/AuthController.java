package com.message.presentation.controller;

import com.message.presentation.dto.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController 
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Intento de inicio de sesión - Usuario: {}", loginRequest.getUsername());
        
        if ("admin".equals(loginRequest.getUsername()) && "1234".equals(loginRequest.getPassword())) {
            logger.info("Login EXITOSO para usuario: {}", loginRequest.getUsername());
            return ResponseEntity.ok("{\"token\": \"fake-jwt-token-123\", \"message\": \"Login Exitoso\"}");
            
        } else {
            logger.warn("Login FALLIDO - Credenciales inválidas para usuario: {}", loginRequest.getUsername());
            return ResponseEntity.status(401).body("{\"message\": \"Credenciales inválidas (Mock)\"}");
        }
    }
}


