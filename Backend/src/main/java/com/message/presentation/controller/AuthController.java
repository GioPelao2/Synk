package com.message.presentation.controller;

import com.message.presentation.dto.LoginRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController 
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        
        // --- Verifica la Conexión ---
        System.out.println("Petición POST /login recibida.");
        System.out.println("Usuario: " + loginRequest.getUsername());
        
        if ("admin".equals(loginRequest.getUsername()) && "1234".equals(loginRequest.getPassword())) {
            
            return ResponseEntity.ok("{\"token\": \"fake-jwt-token-123\", \"message\": \"Login Exitoso\"}");
            
        } else {
            return ResponseEntity.status(401).body("{\"message\": \"Credenciales inválidas (Mock)\"}");
        }
    }
}