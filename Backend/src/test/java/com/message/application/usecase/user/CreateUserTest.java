package com.message.application.usecase.user;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUser createUser;

    @Test
    void execute_ShouldThrowException_IfEmailExists() {
        // Arrange
        String username = "NewUser";
        String email = "exists@synk.com";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(true);
        // Asumimos que el username no existe para que falle en el email
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            createUser.execute(username, email, password);
        });

        verify(userRepository, never()).saveUser(any());
    }

    @Test
    void execute_ShouldSaveUser_WhenDataIsValid() {
        // Arrange
        String username = "ValidUser";
        String email = "valid@synk.com";
        String password = "password123";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // CORRECCIÓN: Interceptamos UserId.temporary() en lugar de newId()
        try (MockedStatic<UserId> mockedUserId = Mockito.mockStatic(UserId.class)) {
            UserId validId = Mockito.mock(UserId.class);
            mockedUserId.when(UserId::temporary).thenReturn(validId);

            // Act
            createUser.execute(username, email, password);

            // Assert
            verify(userRepository).saveUser(any(User.class));
        }
    }
}