package com.message.presentation.controller;

import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.UserRepositoryImpl;
import com.message.presentation.dto.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private UserController userController;

    // =================================================================================
    // PRUEBAS DE CARACTERIZACIÓN PARA 'getOnlineUsers' (Refactoring Candidate)
    // Estas pruebas aseguran que el comportamiento "Mock" actual no cambie inadvertidamente
    // =================================================================================

    @Test
    @DisplayName("getOnlineUsers: Retorna datos Mock incluso si se llama")
    void getOnlineUsers_ReturnsMockData_WhenCalled() {
        // Act
        ResponseEntity<List<UserDTO>> response = userController.getOnlineUsers();

        // Assert
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size(), "Debería retornar exactamente 2 usuarios mock");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("getOnlineUsers: Verifica los datos exactos del primer usuario Mock (Luchito)")
    void getOnlineUsers_ValidatesFirstMockUser() {
        // Act
        ResponseEntity<List<UserDTO>> response = userController.getOnlineUsers();
        UserDTO user1 = response.getBody().get(0);

        // Assert
        assertEquals(1L, user1.getId());
        assertEquals("Luchito", user1.getUsername());
        assertEquals("lucho1234@mock.com", user1.getEmail());
        assertEquals(UserStatus.ONLINE, user1.getStatus());
        assertNull(user1.getLastSeen(), "El usuario mock 1 debería tener lastSeen como null según el código actual");
    }

    @Test
    @DisplayName("getOnlineUsers: Verifica los datos exactos del segundo usuario Mock (Eloysito)")
    void getOnlineUsers_ValidatesSecondMockUser() {
        // Act
        ResponseEntity<List<UserDTO>> response = userController.getOnlineUsers();
        UserDTO user2 = response.getBody().get(1);

        // Assert
        assertEquals(2L, user2.getId());
        assertEquals("Eloysito", user2.getUsername());
        assertEquals("eloy@mock.com", user2.getEmail());
        assertEquals(UserStatus.OFFLINE, user2.getStatus());
        assertNotNull(user2.getLastSeen(), "El usuario mock 2 debería tener una fecha en lastSeen");
    }

    @Test
    @DisplayName("getOnlineUsers: Asegura que no se exponen contraseñas en los Mocks")
    void getOnlineUsers_ShouldNotReturnPasswords() {
        // Act
        ResponseEntity<List<UserDTO>> response = userController.getOnlineUsers();
        List<UserDTO> users = response.getBody();

        // Assert
        assertNull(users.get(0).getPassword());
        assertNull(users.get(1).getPassword());
    }

    @Test
    @DisplayName("getOnlineUsers: Confirma que el Repositorio es ignorado (Prueba de Aislamiento)")
    void getOnlineUsers_IgnoresRepository() {
        // Act
        userController.getOnlineUsers();

        // Assert
        // Esta prueba fallará si en el futuro conectas la BD sin quitar los mocks primero,
        // o pasará confirmando que ahora mismo el repositorio NO se toca.
        verifyNoInteractions(userRepository);
    }

    // =================================================================================
    // PRUEBAS ESTÁNDAR PARA OTROS MÉTODOS (Funcionamiento Normal)
    // Estas pruebas verifican que el resto del controlador sí usa el repositorio
    // =================================================================================

    @Test
    @DisplayName("getUserById: Retorna usuario correctamente cuando existe")
    void getUserById_ReturnsUser_WhenExists() {
        // Arrange
        Long id = 1L;
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(UserId.from(id));
        when(mockUser.getUsername()).thenReturn("TestUser");
        when(mockUser.getEmail()).thenReturn("test@mail.com");
        when(mockUser.getStatus()).thenReturn(UserStatus.ONLINE);

        when(userRepository.findById(UserId.from(id))).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<UserDTO> response = userController.getUserById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("TestUser", response.getBody().getUsername());
        verify(userRepository).findById(UserId.from(id));
    }

    @Test
    @DisplayName("getUserById: Retorna 404 cuando el usuario no existe")
    void getUserById_ReturnsNotFound_WhenUserDoesNotExist() {
        // Arrange
        Long id = 999L;
        when(userRepository.findById(UserId.from(id))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserDTO> response = userController.getUserById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("registerUser: Crea usuario exitosamente")
    void registerUser_CreatesUser_WhenValid() {
        // Arrange
        UserDTO requestDto = new UserDTO("NewUser", "new@mail.com", "pass123");

        User savedUserDomain = mock(User.class);
        when(savedUserDomain.getId()).thenReturn(UserId.from(10L));
        when(savedUserDomain.getUsername()).thenReturn("NewUser");
        when(savedUserDomain.getEmail()).thenReturn("new@mail.com");

        when(userRepository.existsByUsername("NewUser")).thenReturn(false);
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(userRepository.saveUser(any(User.class))).thenReturn(savedUserDomain);

        // Act
        ResponseEntity<UserDTO> response = userController.registerUser(requestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());
        verify(userRepository).saveUser(any(User.class));
    }
}