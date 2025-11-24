package com.message.presentation.controller;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.MessageRepositoryImpl;
import com.message.infrastructure.persistence.UserRepositoryImpl;
import com.message.presentation.dto.MessageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageRepositoryImpl messageRepository;
    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private MessageController messageController;

    @Test
    void sendMessage_Success_WhenUsersExist() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;
        String content = "Hola mundo";
        MessageDTO requestDto = new MessageDTO(senderId, receiverId, content);

        User mockSender = mock(User.class);
        User mockReceiver = mock(User.class);
        Message mockSavedMessage = mock(Message.class); // Entidad guardada

        when(mockSender.getUsername()).thenReturn("user1");
        when(mockReceiver.getUsername()).thenReturn("user2");
        // Simular que el repositorio encuentra a los usuarios
        when(userRepository.findById(any(UserId.class))).thenReturn(Optional.of(mockSender)).thenReturn(Optional.of(mockReceiver));
        // Simular guardado
        when(messageRepository.save(any(Message.class))).thenReturn(mockSavedMessage);

        // Mocks adicionales para la respuesta
        when(mockSavedMessage.getMessageId()).thenReturn(com.message.domain.valueobjects.MessageId.from(100L));
        when(mockSavedMessage.getSenderId()).thenReturn(UserId.from(senderId));
        when(mockSavedMessage.getReceiverId()).thenReturn(UserId.from(receiverId));
        when(mockSavedMessage.getContent()).thenReturn(content);

        // Act
        ResponseEntity<MessageDTO> response = messageController.sendMessage(requestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        // Verificamos que la lógica de negocio se ejecutó (buscar usuarios y guardar)
        verify(userRepository, times(2)).findById(any(UserId.class));
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void sendMessage_ReturnsBadRequest_WhenContentIsEmpty() {
        // Arrange
        MessageDTO requestDto = new MessageDTO(1L, 2L, ""); // Contenido vacío

        // Act
        ResponseEntity<MessageDTO> response = messageController.sendMessage(requestDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(messageRepository); // No se debe guardar nada
    }
}