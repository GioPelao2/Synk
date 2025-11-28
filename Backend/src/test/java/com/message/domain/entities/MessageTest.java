package com.message.domain.entities;

import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad Message enfocadas en el Code Smell 'Dispensables'.
 * Valida que la lógica de negocio (identificación de usuarios) funcione
 * independientemente de la presencia o ausencia de comentarios explicativos.
 */
class MessageDispensablesTest {

    /**
     * PRUEBA 1: Verificar identificación del remitente (isFromUser).
     * Situación: El refactoring eliminó el comentario "// Helper para verificar...".
     * Estrategia: Usamos el constructor completo (con ID 1L) para evitar el error
     * de validación por ID negativo (-1L) presente en los constructores simples.
     */
    @Test
    void testIsFromUser_IdentifiesSenderCorrectly() {
        // Arrange
        UserId senderId = UserId.from(10L);
        UserId receiverId = UserId.from(20L);
        MessageId validMessageId = MessageId.from(1L); // ID positivo para evitar IllegalArgumentException
        String content = "Prueba de Sender";

        // Usamos el constructor que acepta MessageId explícito
        Message message = new Message(validMessageId, senderId, receiverId, content);

        // Act & Assert
        assertTrue(message.isFromUser(senderId),
                "El método isFromUser debe retornar TRUE para el ID del sender correcto.");
        assertFalse(message.isFromUser(receiverId),
                "El método isFromUser debe retornar FALSE para cualquier otro ID.");
    }

    /**
     * PRUEBA 2: Verificar identificación del destinatario (isToUser).
     * Situación: El refactoring eliminó comentarios redundantes.
     * Estrategia: Igual que la anterior, inyectamos un ID válido.
     */
    @Test
    void testIsToUser_IdentifiesReceiverCorrectly() {
        // Arrange
        UserId senderId = UserId.from(10L);
        UserId receiverId = UserId.from(20L);
        MessageId validMessageId = MessageId.from(1L); // ID positivo
        String content = "Prueba de Receiver";

        Message message = new Message(validMessageId, senderId, receiverId, content);

        // Act & Assert
        assertTrue(message.isToUser(receiverId),
                "El método isToUser debe retornar TRUE para el ID del receiver correcto.");
        assertFalse(message.isToUser(senderId),
                "El método isToUser debe retornar FALSE para el sender.");
    }
}