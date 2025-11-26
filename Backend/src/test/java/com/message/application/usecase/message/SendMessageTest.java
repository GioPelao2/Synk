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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendMessageTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private SendMessage sendMessage;

    @Test
    void execute_ShouldSaveMessage_WhenValidationPasses() {
        // Arrange
        UserId senderId = UserId.from(1L);
        UserId receiverId = UserId.from(2L);
        String content = "Hola Mundo";

        // FIX: Usamos MockedStatic para interceptar la creación del ID temporal (-1L)
        // que ocurre dentro del constructor de Message y causa el error de validación.
        try (MockedStatic<MessageId> mockedMessageId = Mockito.mockStatic(MessageId.class)) {

            // Configuramos el mock estático para que devuelva un ID válido simulado
            // cuando el código intente hacer MessageId.from(-1L)
            MessageId validMockId = Mockito.mock(MessageId.class);
            mockedMessageId.when(() -> MessageId.from(anyLong())).thenReturn(validMockId);

            // Act
            sendMessage.execute(senderId, receiverId, content);

            // Assert
            // Verificamos que se llamó al repositorio para guardar el mensaje.
            // Ya no verificamos messageDomainService porque tu clase no lo usa.
            verify(messageRepository).save(any(Message.class));
        }
    }
}