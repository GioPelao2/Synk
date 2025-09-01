package com.message.domain.entities;

import com.message.domain.enums.MessageStatus;
import com.message.domain.enums.MessageType;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private MessageId messageId;
    private UserId senderId;
    private UserId receiverId;
    private String content;
    private LocalDateTime timestamp;
    private MessageType messageType;
    private MessageStatus status;

    // Constructor para crear mensajes nuevos (sin ID)
    public Message(UserId senderId, UserId receiverId, String content) {
        this.messageId = null; // deberia ser asignado por el repository c:
        this.senderId = Objects.requireNonNull(senderId, "SenderId cannot be null");
        this.receiverId = Objects.requireNonNull(receiverId, "ReceiverId cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.SENT;
    }

    // Constructor para reconstruir mensajes existentes (con ID) - usado por repositories
    public Message(MessageId messageId, UserId senderId, UserId receiverId, String content) {
        this.messageId = messageId;
        this.senderId = Objects.requireNonNull(senderId, "SenderId cannot be null");
        this.receiverId = Objects.requireNonNull(receiverId, "ReceiverId cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.SENT;
    }

    // Constructor completo para reconstruir desde persistencia
    public Message(MessageId messageId, UserId senderId, UserId receiverId, 
                   String content, LocalDateTime timestamp, MessageStatus status) {
        this.messageId = messageId;
        this.senderId = Objects.requireNonNull(senderId, "SenderId cannot be null");
        this.receiverId = Objects.requireNonNull(receiverId, "ReceiverId cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    // Método para asignar ID (usado por repository)
    public void assignId(MessageId messageId) {
        if (this.messageId != null) {
            throw new IllegalStateException("Message already has an ID assigned");
        }
        this.messageId = Objects.requireNonNull(messageId, "MessageId cannot be null");
    }

    public void markAsRead() {
        this.status = MessageStatus.READ;
    }

    public void validate() {
        if (this.senderId == null) {
            throw new IllegalArgumentException("SenderId cannot be null");
        }
        if (this.receiverId == null) {
            throw new IllegalArgumentException("ReceiverId cannot be null");
        }
        if (this.content == null || this.content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
    }

    public boolean isFromUser(UserId userId) {
        return this.senderId.equals(userId);
    }

    // Getters
    public MessageId getMessageId() { return messageId; }
    public UserId getSenderId() { return senderId; }
    public UserId getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public MessageStatus getStatus() { return status; }
    public MessageType getMessageType() { return messageType; }
}
