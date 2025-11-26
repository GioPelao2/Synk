package com.message.application.usecase.message;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetConversationHistoryTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private GetConversationHistory getConversationHistory;

    @Test
    void execute_ShouldReturnList_WhenUsersAreDifferent() {
        // Arrange
        UserId user1 = UserId.from(1L);
        UserId user2 = UserId.from(2L);
        List<Message> mockHistory = List.of(mock(Message.class));

        // Simulamos la respuesta del repositorio
        when(messageRepository.findConversationHistory(user1, user2)).thenReturn(mockHistory);

        // Act
        List<Message> result = getConversationHistory.execute(user1, user2);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(messageRepository).findConversationHistory(user1, user2);
    }

    @Test
    void execute_ShouldThrowException_WhenUsersAreSame() {
        // Arrange
        UserId user1 = UserId.from(1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getConversationHistory.execute(user1, user1);
        });

        verify(messageRepository, never()).findConversationHistory(any(), any());
    }
}