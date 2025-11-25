package com.message.application.service;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.MessageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepositoryImpl messageRepositoryImpl;

    @InjectMocks
    private MessageService messageService;

    private UserId senderId;
    private UserId receiverId;
    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        senderId = UserId.from(1L);
        receiverId = UserId.from(2L);

        // Configuramos usuarios básicos para las pruebas
        sender = mock(User.class);
        receiver = mock(User.class);

        when(sender.getId()).thenReturn(senderId);
        when(receiver.getId()).thenReturn(receiverId);
    }

    // --- Pruebas para sendMessage (Validaciones exhaustivas) ---

    @Test
    @DisplayName("sendMessage: Debe lanzar excepción si el contenido es nulo o vacío")
    void sendMessage_Throws_WhenContentInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage(senderId, receiverId, null)
        );
        assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage(senderId, receiverId, "   ")
        );
    }

    @Test
    @DisplayName("sendMessage: Debe lanzar excepción si el emisor (sender) no existe")
    void sendMessage_Throws_WhenSenderNotFound() {
        when(userRepository.findById(senderId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage(senderId, receiverId, "Hola")
        );
        assertTrue(ex.getMessage().contains("Sender not found"));
    }

    @Test
    @DisplayName("sendMessage: Debe lanzar excepción si el receptor (receiver) no existe")
    void sendMessage_Throws_WhenReceiverNotFound() {
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage(senderId, receiverId, "Hola")
        );
        assertTrue(ex.getMessage().contains("Receiver not found"));
    }

    @Test
    @DisplayName("sendMessage: Debe lanzar excepción si se intenta enviar mensaje a uno mismo")
    void sendMessage_Throws_WhenSelfMessage() {
        // Configuramos mocks para que existan, pero usamos el mismo ID
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));

        // Nota: El servicio verifica senderId.equals(receiverId) antes de buscar en repo a veces,
        // o después. Según tu código es después de los findById.
        // Asumimos que pasamos el mismo ID en los argumentos.

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage(senderId, senderId, "Hola a mi mismo")
        );
        assertTrue(ex.getMessage().contains("yourself"));
    }

    @Test
    @DisplayName("sendMessage: Debe lanzar excepción si el receptor no puede recibir mensajes (bloqueo/estado)")
    void sendMessage_Throws_WhenReceiverCannotReceive() {
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        // Simulamos que el receptor rechaza el mensaje (ej. está OFFLINE y la lógica lo prohíbe)
        when(receiver.canReceiveMessage(senderId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                messageService.sendMessage(senderId, receiverId, "Hola")
        );
    }

    @Test
    @DisplayName("sendMessage: Éxito al enviar mensaje válido")
    void sendMessage_Success() {
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(receiver.canReceiveMessage(senderId)).thenReturn(true);

        Message savedMessage = mock(Message.class);
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message result = messageService.sendMessage(senderId, receiverId, "Hola mundo");

        assertNotNull(result);
        verify(messageRepository).save(any(Message.class));
    }

    // --- Pruebas para getConversationHistory ---

    @Test
    @DisplayName("getConversationHistory: Debe lanzar excepción si algún usuario no existe")
    void getConversationHistory_Throws_WhenUserNotFound() {
        when(userRepository.findById(senderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                messageService.getConversationHistory(senderId, receiverId)
        );
    }

    // --- Pruebas para markMessageAsRead ---

    @Test
    @DisplayName("markMessageAsRead: Debe lanzar excepción si el mensaje no existe")
    void markMessageAsRead_Throws_WhenMessageNotFound() {
        MessageId msgId = MessageId.from(999L);
        when(messageRepository.findById(msgId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                messageService.markMessageAsRead(msgId, receiverId)
        );
    }

    @Test
    @DisplayName("markMessageAsRead: Debe lanzar excepción si el usuario no es el receptor del mensaje")
    void markMessageAsRead_Throws_WhenUserIsNotReceiver() {
        MessageId msgId = MessageId.from(100L);
        Message mockMessage = mock(Message.class);

        when(messageRepository.findById(msgId)).thenReturn(Optional.of(mockMessage));
        // El mensaje iba para OTRO usuario (ej. ID 5), no para 'receiverId' (ID 2)
        when(mockMessage.getReceiverId()).thenReturn(UserId.from(5L));

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                messageService.markMessageAsRead(msgId, receiverId)
        );
        assertTrue(ex.getMessage().contains("not the receiver"));
    }

    @Test
    @DisplayName("markMessageAsRead: Éxito al marcar como leído")
    void markMessageAsRead_Success() {
        MessageId msgId = MessageId.from(100L);
        Message mockMessage = mock(Message.class);

        when(messageRepository.findById(msgId)).thenReturn(Optional.of(mockMessage));
        when(mockMessage.getReceiverId()).thenReturn(receiverId); // Coincide

        messageService.markMessageAsRead(msgId, receiverId);

        // Verifica que llama al método específico de la implementación
        verify(messageRepositoryImpl).markMessageAsRead(msgId);
    }

    // --- Pruebas para deleteMessage (Validación de sender) ---

    @Test
    @DisplayName("deleteMessage: Debe lanzar excepción si el usuario no es el remitente")
    void deleteMessage_Throws_WhenUserIsNotSender() {
        MessageId msgId = MessageId.from(100L);
        Message mockMessage = mock(Message.class);

        when(messageRepository.findById(msgId)).thenReturn(Optional.of(mockMessage));
        // El mensaje fue enviado por OTRO (ID 5), senderId (ID 1) intenta borrarlo
        when(mockMessage.getSenderId()).thenReturn(UserId.from(5L));

        assertThrows(IllegalArgumentException.class, () ->
                messageService.deleteMessage(msgId, senderId)
        );
    }

    @Test
    @DisplayName("deleteMessage: Debe lanzar UnsupportedOperationException (por ahora)")
    void deleteMessage_Throws_NotImplemented() {
        MessageId msgId = MessageId.from(100L);
        Message mockMessage = mock(Message.class);

        when(messageRepository.findById(msgId)).thenReturn(Optional.of(mockMessage));
        when(mockMessage.getSenderId()).thenReturn(senderId); // Es el dueño

        // Tal como está el código actual
        assertThrows(UnsupportedOperationException.class, () ->
                messageService.deleteMessage(msgId, senderId)
        );
    }
}