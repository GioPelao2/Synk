package com.message.domain.entities;

import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    // Datos de prueba comunes
    private final UserId senderId = UserId.from(1L);
    private final UserId receiverId = UserId.from(2L);
    private final String validContent = "Hola, ¿cómo estás?";

    @Test
    @DisplayName("Debe crear un mensaje válido correctamente")
    void create_ValidMessage_Success() {
        Message message = new Message(senderId, receiverId, validContent);

        assertDoesNotThrow(message::validate);
        assertEquals(validContent, message.getContent());
        assertFalse(message.isRead());
        assertNotNull(message.getTimestamp());
        // Verifica que el ID temporal sea -1
        assertEquals(-1L, message.getMessageId().value());
    }

    // --- Pruebas para los IFs de Validación de Contenido ---

    @Test
    @DisplayName("Debe lanzar excepción si el contenido es nulo")
    void validate_Throws_WhenContentIsNull() {
        // Nota: El constructor ya protege contra nulos, pero validamos el comportamiento
        assertThrows(NullPointerException.class, () ->
                new Message(senderId, receiverId, null)
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción si el contenido está vacío o son solo espacios")
    void validate_Throws_WhenContentIsEmptyOrBlank() {
        Message emptyMessage = new Message(senderId, receiverId, "");
        Message blankMessage = new Message(senderId, receiverId, "   ");

        IllegalArgumentException exceptionEmpty = assertThrows(IllegalArgumentException.class, emptyMessage::validate);
        assertEquals("Content cannot be empty", exceptionEmpty.getMessage());

        IllegalArgumentException exceptionBlank = assertThrows(IllegalArgumentException.class, blankMessage::validate);
        assertEquals("Content cannot be empty", exceptionBlank.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el contenido excede 1000 caracteres")
    void validate_Throws_WhenContentIsTooLong() {
        // Generamos un string de 1001 caracteres
        String longContent = "a".repeat(1001);
        Message message = new Message(senderId, receiverId, longContent);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, message::validate);
        assertTrue(exception.getMessage().contains("exceeds maximum length"));
    }

    // --- Pruebas para los IFs de Validación de Usuarios ---

    @Test
    @DisplayName("Debe lanzar excepción si el sender y receiver son el mismo")
    void validate_Throws_WhenSenderEqualsReceiver() {
        Message message = new Message(senderId, senderId, validContent); // Mismo ID

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, message::validate);
        assertEquals("Sender and receiver cannot be the same user", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si senderId o receiverId son nulos")
    void validate_Throws_WhenIdsAreNull() {
        assertThrows(NullPointerException.class, () ->
                new Message(null, receiverId, validContent)
        );
        assertThrows(NullPointerException.class, () ->
                new Message(senderId, null, validContent)
        );
    }

    // --- Pruebas para los IFs de Validación de Timestamp e ID ---

    @Test
    @DisplayName("Debe lanzar excepción si el timestamp está en el futuro")
    void validate_Throws_WhenTimestampIsInFuture() {
        // Usamos el constructor completo para inyectar una fecha futura
        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        Message message = new Message(
                MessageId.from(1L),
                senderId,
                receiverId,
                validContent,
                futureTime,
                false
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, message::validate);
        assertEquals("Timestamp cannot be in the future", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el MessageId es negativo (excepto -1)")
    void validate_Throws_WhenMessageIdIsInvalid() {
        // ID temporal válido (-1)
        Message tempMessage = new Message(senderId, receiverId, validContent);
        assertDoesNotThrow(tempMessage::validate);

        // ID inválido (-5), simulamos inyección forzada o error de BD
        // Nota: MessageId.from() podría validar esto, pero probamos la lógica de la entidad Message
        try {
            Message invalidMessage = new Message(
                    MessageId.from(-5L), // Supongamos que el ValueObject lo permite para este test
                    senderId,
                    receiverId,
                    validContent,
                    LocalDateTime.now(),
                    false
            );
            // Si MessageId permite negativos, Message.validate debería atraparlo
            assertThrows(IllegalArgumentException.class, invalidMessage::validate);
        } catch (IllegalArgumentException e) {
            // Si MessageId ya valida negativos en su constructor, la prueba también es exitosa
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("withId() debe lanzar excepción si ya tiene un ID asignado")
    void withId_Throws_IfIdAlreadyAssigned() {
        Message message = new Message(senderId, receiverId, validContent);
        MessageId newId = MessageId.from(100L);

        // Primera asignación: debería funcionar
        Message savedMessage = message.withId(newId);
        assertEquals(100L, savedMessage.getMessageId().value());

        // Segunda asignación sobre el mensaje ya guardado: debe fallar (el IF dentro de withId)
        assertThrows(IllegalStateException.class, () -> savedMessage.withId(MessageId.from(200L)));
    }
}