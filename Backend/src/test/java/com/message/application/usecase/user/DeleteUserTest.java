package com.message.application.usecase.user;

import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUser deleteUser;

    @Test
    void execute_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        UserId userId = UserId.from(99L);
        // Simulamos que el usuario NO existe
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            deleteUser.execute(userId);
        });

        // Verificamos que no se llamó al método delete
        verify(userRepository, never()).deleteById(any());
    }
}