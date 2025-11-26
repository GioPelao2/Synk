package com.message.application.service;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.ConversationId; // Importante
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.MessageRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock private MessageRepository messageRepository;
    @Mock private UserRepository userRepository;
    @Mock private MessageRepositoryImpl messageRepositoryImpl;

    @InjectMocks
    private MessageService messageService;

    @Test
    void sendMessage_EmptyContent_ShouldThrowException() {
        // Arrange
        UserId senderId = UserId.from(1L);
        UserId receiverId = UserId.from(2L);
        String emptyContent = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(senderId, receiverId, emptyContent);
        });
    }

    @Test
    void sendMessage_ValidContent_ShouldCallRepositorySave() {
        // Arrange
        UserId senderId = UserId.from(1L);
        UserId receiverId = UserId.from(2L);
        String content = "Hola mundo";

        User sender = mock(User.class);
        User receiver = mock(User.class);
        // Aseguramos que el receptor pueda recibir mensajes
        when(receiver.canReceiveMessage(senderId)).thenReturn(true);

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        // FIX: Mockeamos AMBOS: MessageId y ConversationId para evitar errores de IDs negativos
        // Usamos try-with-resources anidado para asegurar que ambos mocks se cierren
        try (MockedStatic<MessageId> mockedMessageId = Mockito.mockStatic(MessageId.class);
             MockedStatic<ConversationId> mockedConversationId = Mockito.mockStatic(ConversationId.class)) {

            // 1. Configurar Mock de MessageId
            MessageId validMessageId = Mockito.mock(MessageId.class);
            // Interceptamos cualquier creación estática
            mockedMessageId.when(() -> MessageId.from(anyLong())).thenReturn(validMessageId);

            // 2. Configurar Mock de ConversationId (CRÍTICO: Message también crea esto internamente)
            ConversationId validConversationId = Mockito.mock(ConversationId.class);
            mockedConversationId.when(() -> ConversationId.from(anyLong())).thenReturn(validConversationId);
            // Si existe un método newId() o temporary(), también lo interceptamos:
            mockedConversationId.when(ConversationId::newId).thenReturn(validConversationId);

            // Act
            messageService.sendMessage(senderId, receiverId, content);

            // Assert
            verify(messageRepository, times(1)).save(any(Message.class));
        }
    }

    @Test
    void getConversationHistory_ShouldReturnList() {
        // Arrange
        UserId user1 = UserId.from(1L);
        UserId user2 = UserId.from(2L);

        // FIX: Usamos mock(Message.class) para evitar que se ejecute el constructor de Message.
        // Esto evita completamente los errores de validación de IDs durante la instanciación en el test.
        Message mockMessage = mock(Message.class);
        List<Message> mockHistory = List.of(mockMessage);

        when(userRepository.findById(user1)).thenReturn(Optional.of(mock(User.class)));
        when(userRepository.findById(user2)).thenReturn(Optional.of(mock(User.class)));
        when(messageRepository.findConversationHistory(user1, user2)).thenReturn(mockHistory);

        // Act
        List<Message> result = messageService.getConversationHistory(user1, user2);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}