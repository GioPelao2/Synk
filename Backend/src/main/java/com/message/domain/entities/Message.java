package com.message.domain.entities;

import com.message.domain.enums.MessageType;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * representa un mensaje entre usuarios
 * NOTA: hay varios constructores porque agregue funcionalidades sobre la marcha
 */
public class Message {
    private MessageId messageId;
    private UserId senderId;
    private UserId receiverId;
    private String content; // TODO: Validar longitud máxima del contenido
    private LocalDateTime timestamp;
    private MessageType messageType;
    private boolean isRead;

    // Constructor para cuando se trae mensajes desde la BD
    public Message(MessageId messageId, UserId senderId, UserId receiverId, String content, LocalDateTime timestamp, boolean isRead) {
        this.messageId = Objects.requireNonNull(messageId, "MessageId cannot be null");
        this.senderId = Objects.requireNonNull(senderId, "SenderId cannot be null");
        this.receiverId = Objects.requireNonNull(receiverId, "ReceiverId cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        this.messageType = messageType.TEXT; //Arreglar?
        this.isRead = isRead;
    }

    // Constructor para mensajes nuevos (más común)
    public Message(UserId senderId, UserId receiverId, String content) {
        this.messageId = MessageId.from(-1L); // temporal hasta que la BD le asigne el ID real
        this.senderId = Objects.requireNonNull(senderId, "SenderId cannot be null");
        this.receiverId = Objects.requireNonNull(receiverId, "ReceiverId cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.timestamp = LocalDateTime.now();
        this.isRead = false; // Obvio que empieza sin leer
    }

    // Constructor que cree para el mapper (probablemente se puede refactorizar)
    public Message(MessageId messageId, UserId senderId, UserId receiverId, String content) {
        this.messageId = Objects.requireNonNull(messageId, "MessageId cannot be null");
        this.senderId = Objects.requireNonNull(senderId, "SenderId cannot be null");
        this.receiverId = Objects.requireNonNull(receiverId, "ReceiverId cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    /**
     * Método para asignar un ID real después de guardar en BD
     * feo pero funciona bien
     */
    public Message withId(MessageId newId) {
        if (!this.messageId.value().equals(-1L)) {
            throw new IllegalStateException("Message already has an ID assigned");
        }
        return new Message(newId, this.senderId, this.receiverId, this.content, this.timestamp, this.isRead);
    }

    public void markAsRead() {
        this.isRead = true;
    }

    // Helper para verificar si el mensaje es de un usuario especifico
    public boolean isFromUser(UserId userId) {
        return this.senderId.equals(userId);
    }

    // Helper para verificar si el mensaje es para un usuario especifico
    public boolean isToUser(UserId userId) {
        return this.receiverId.equals(userId);
    }

    public void validate() {
        if (messageId == null) {
            throw new IllegalArgumentException("MessageId cannot be null");
        }
        if (senderId == null) {
            throw new IllegalArgumentException("SenderId cannot be null");
        }
        if (receiverId == null) {
            throw new IllegalArgumentException("ReceiverId cannot be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }

        if (content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        final int MAX_CONTENT_LENGTH = 1000;
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("Content exceeds maximum length of " + MAX_CONTENT_LENGTH + " characters");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same user");
        }

        if (timestamp.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Timestamp cannot be in the future");
        }

        if (messageId.value() < -1) {
            throw new IllegalArgumentException("MessageId cannot be negative (except -1 for temporary IDs)");
        }
    }

    public MessageId getMessageId() { return messageId; }
    public UserId getSenderId() { return senderId; }
    public UserId getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Message message = (Message) object;
        return Objects.equals(messageId, message.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' + // TODO: Truncar contenido muy largo en toString()
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                '}';
    }
}
