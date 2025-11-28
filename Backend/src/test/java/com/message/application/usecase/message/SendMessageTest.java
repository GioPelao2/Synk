package com.message.application.usecase.message;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.MessageId;
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
class SendMessageTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private SendMessage sendMessage;

    @Test
    void execute_ShouldSaveMessage_WhenContentIsValid() {
        // Arrange
        UserId senderId = UserId.from(1L);
        UserId receiverId = UserId.from(2L);
        String content = "Hola UseCase";

        // FIX: Interceptamos la creación del ID temporal (-1L) para evitar error de validación
        try (MockedStatic<MessageId> mockedMessageId = Mockito.mockStatic(MessageId.class)) {
            MessageId validId = Mockito.mock(MessageId.class);
            mockedMessageId.when(() -> MessageId.from(anyLong())).thenReturn(validId);

            // Act
            sendMessage.execute(senderId, receiverId, content);

            // Assert
            verify(messageRepository).save(any(Message.class));
        }
    }

    @Test
    void execute_ShouldThrowException_WhenContentIsEmpty() {
        // Arrange
        UserId senderId = UserId.from(1L);
        UserId receiverId = UserId.from(2L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sendMessage.execute(senderId, receiverId, "");
        });

        verify(messageRepository, never()).save(any());
    }
}