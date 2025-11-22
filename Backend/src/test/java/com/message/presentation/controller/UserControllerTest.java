package com.message.presentation.controller;

import com.message.domain.enums.UserStatus;
import com.message.infrastructure.persistence.UserRepositoryImpl;
import com.message.presentation.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepositoryImpl userRepository; // Se mockea aunque el código actual lo ignore

    @InjectMocks
    private UserController userController;

    @Test
    void getOnlineUsers_ReturnsMockData_WhenCalled() {
        // Arrange: No configuramos el mock porque sabemos que el controlador lo ignora actualmente

        // Act
        ResponseEntity<List<UserDTO>> response = userController.getOnlineUsers();

        // Assert
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size()); // Sabemos que devuelve 2 usuarios mock

        // Validamos que devuelve los datos "quemados" en código
        UserDTO user1 = response.getBody().get(0);
        assertEquals("Luchito", user1.getUsername());
        assertEquals(UserStatus.ONLINE, user1.getStatus());

        // Esta aserción prueba que el repositorio NO se está llamando (el problema a refactorizar)
        verifyNoInteractions(userRepository);
    }
}