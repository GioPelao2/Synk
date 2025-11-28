package com.message.application.usecase.conversation;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateConversationTest {

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private CreateConversation createConversation;

    @Test
    void execute_ShouldThrowException_WhenConversationAlreadyExists() {
        // Arrange
        UserId user1 = UserId.from(1L);
        UserId user2 = UserId.from(2L);

        // Simulamos que el repositorio dice que YA existe una conversación
        when(conversationRepository.existsBetweenUsers(user1, user2)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            createConversation.execute(user1, user2);
        });

        // Aseguramos que NO se intentó guardar nada nuevo
        verify(conversationRepository, never()).save(any(Conversation.class));
    }
}