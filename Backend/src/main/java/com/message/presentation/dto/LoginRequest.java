package com.message.presentation.dto;

import lombok.Data; 


import jakarta.validation.constraints.NotBlank;

@Data 
public class LoginRequest {
    
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}