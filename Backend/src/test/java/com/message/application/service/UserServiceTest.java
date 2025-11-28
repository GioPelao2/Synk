package com.message.application.service;

import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_IfEmailExists_ShouldThrowException() {
        // Arrange
        String username = "NewUser";
        String existingEmail = "test@synk.com";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(username, existingEmail);
        });

        verify(userRepository, never()).saveUser(any(User.class));
    }

    @Test
    void getUserById_WhenExists_ShouldReturnUser() {
        // Arrange
        UserId id = UserId.from(1L);
        // FIX: Usamos mock(User.class) para evitar invocar el constructor que falla
        User expectedUser = mock(User.class);
        when(expectedUser.getUsername()).thenReturn("Juan");

        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> result = userService.getUserById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getUsername());
    }

    @Test
    void setUserOnline_ShouldUpdateAndSaveUser() {
        // Arrange
        UserId id = UserId.from(1L);
        // FIX: Mockeamos el User para evitar el error de UserId.newId() en el constructor
        User userMock = mock(User.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(userMock));
        when(userRepository.saveUser(any(User.class))).thenReturn(userMock);

        // Act
        userService.setUserOnline(id);

        // Assert
        // Verificamos que se llamó al método goOnline() en el mock
        verify(userMock).goOnline();
        // Verificamos que se guardó
        verify(userRepository, times(1)).saveUser(userMock);
    }
}