package com.message.application.usecase.user;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SetUserOnlineTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SetUserOnline setUserOnline;

    @Test
    void execute_ShouldCallGoOnlineAndSave() {
        // Arrange
        UserId userId = UserId.from(1L);
        User userMock = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));

        // Act
        setUserOnline.execute(userId);

        // Assert
        verify(userMock).goOnline();      // Verificamos que se cambió el estado en la entidad
        verify(userRepository).saveUser(userMock); // Verificamos que se guardó
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        UserId userId = UserId.from(99L);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            setUserOnline.execute(userId);
        });

        verify(userRepository, never()).saveUser(any());
    }
}