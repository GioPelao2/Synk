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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserByIdTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserById getUserById;

    @Test
    void execute_ShouldReturnUser_WhenExists() {
        // Arrange
        UserId id = UserId.from(1L);
        User expectedUser = mock(User.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> result = getUserById.execute(id);

        // Assert
        assertTrue(result.isPresent());
        verify(userRepository).findById(id);
    }

    @Test
    void execute_ShouldThrowException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            getUserById.execute(null);
        });
    }
}