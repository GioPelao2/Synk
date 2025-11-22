package com.message.application.usecase;

import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.repositories.UserRepository;
import com.message.domain.service.UserDomainService;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserStatusTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDomainService userDomainService;

    @InjectMocks
    private UpdateUserStatus updateUserStatus;

    @Test
    void execute_SetsStatusOnline_WhenEnumIsOnline() {
        // Arrange
        UserId userId = UserId.from(1L);
        User mockUser = mock(User.class);
        when(mockUser.getStatus()).thenReturn(UserStatus.OFFLINE); // Estado inicial diferente
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userDomainService.isValidUserStatus(mockUser)).thenReturn(true);

        // Act
        updateUserStatus.execute(userId, UserStatus.ONLINE);

        // Assert
        verify(mockUser).goOnline(); // Verificamos que se llamó al método correcto del switch
        verify(userRepository).saveUser(mockUser);
    }

    @Test
    void execute_SetsStatusOffline_WhenEnumIsOffline() {
        // Arrange
        UserId userId = UserId.from(1L);
        User mockUser = mock(User.class);
        when(mockUser.getStatus()).thenReturn(UserStatus.ONLINE);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userDomainService.isValidUserStatus(mockUser)).thenReturn(true);

        // Act
        updateUserStatus.execute(userId, UserStatus.OFFLINE);

        // Assert
        verify(mockUser).goOffline(); // Verifica el caso OFFLINE del switch
        verify(userRepository).saveUser(mockUser);
    }
}