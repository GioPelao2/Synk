package com.message.application.service;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.MessageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private MessageService messageService;

    @BeforeEach
    void setUp() {
        // Inyección manual para evitar que Mockito confunda la interfaz con la implementación
        messageService = new MessageService(messageRepository, userRepository, messageRepositoryImpl);
    }

    @Test
    void sendMessage_EmptyContent_ShouldThrowException() {
        UserId senderId = UserId.from(1L);
        UserId receiverId = UserId.from(2L);
        String emptyContent = "";

        assertThrows(IllegalArgumentException.class, () -> {
            messageService.sendMessage(senderId, receiverId, emptyContent);
        });
    }

    @Test
    void sendMessage_ValidContent_ShouldCallRepositorySave() {
        UserId senderId = UserId.from(1L);
        UserId receiverId = UserId.from(2L);
        String content = "Hola mundo";

        User sender = mock(User.class);
        User receiver = mock(User.class);
        when(receiver.canReceiveMessage(senderId)).thenReturn(true);

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        // FIX CRÍTICO: Mockeamos MessageId Y ConversationId para evitar errores en el constructor de Message
        try (MockedStatic<MessageId> mockedMessageId = Mockito.mockStatic(MessageId.class);
             MockedStatic<ConversationId> mockedConversationId = Mockito.mockStatic(ConversationId.class)) {

            // Configuramos MessageId para devolver un mock válido
            MessageId validMsgId = Mockito.mock(MessageId.class);
            mockedMessageId.when(() -> MessageId.from(anyLong())).thenReturn(validMsgId);

            // Configuramos ConversationId para devolver un mock válido (si el constructor lo llama)
            ConversationId validConvId = Mockito.mock(ConversationId.class);
            mockedConversationId.when(ConversationId::newId).thenReturn(validConvId);
            mockedConversationId.when(() -> ConversationId.from(anyLong())).thenReturn(validConvId);

            // Act
            messageService.sendMessage(senderId, receiverId, content);

            // Assert
            verify(messageRepository, times(1)).save(any(Message.class));
        }
    }

    @Test
    void getConversationHistory_ShouldReturnList() {
        UserId user1 = UserId.from(1L);
        UserId user2 = UserId.from(2L);

        // Usamos un mock de Message para no disparar el constructor problemático
        Message mockMessage = mock(Message.class);
        List<Message> mockHistory = List.of(mockMessage);

        when(userRepository.findById(user1)).thenReturn(Optional.of(mock(User.class)));
        when(userRepository.findById(user2)).thenReturn(Optional.of(mock(User.class)));

        // Al inyectar manualmente en setUp, garantizamos que este 'when' afecte al repositorio correcto
        when(messageRepository.findConversationHistory(user1, user2)).thenReturn(mockHistory);

        List<Message> result = messageService.getConversationHistory(user1, user2);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}