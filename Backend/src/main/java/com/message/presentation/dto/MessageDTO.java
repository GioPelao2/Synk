package com.message.presentation.dto;

import com.message.domain.entities.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

public class MessageDTO {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    // info adicional del sender/receiver en responses
    private String senderUsername;
    private String receiverUsername;


    // Constructor desde entidad Message (para responses)
    public MessageDTO(Message message) {
        this.id = message.getMessageId().value();
        this.senderId = message.getSenderId().value();
        this.receiverId = message.getReceiverId().value();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
        this.isRead = message.isRead();
    }

    // Constructor para requests (enviar mensaje nuevo)
    public MessageDTO(Long senderId, Long receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }

    // Constructor completo con info de usuarios
    public MessageDTO(Long id, Long senderId, Long receiverId, String content,
                     LocalDateTime timestamp, boolean isRead,
                     String senderUsername, String receiverUsername) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    // Método para crear DTO enriquecido con info de usuarios
    public static MessageDTO withUserInfo(Message message, String senderUsername, String receiverUsername) {
        return new MessageDTO(
            message.getMessageId().value(),
            message.getSenderId().value(),
            message.getReceiverId().value(),
            message.getContent(),
            message.getTimestamp(),
            message.isRead(),
            senderUsername,
            receiverUsername
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public String getReceiverUsername() { return receiverUsername; }
    public void setReceiverUsername(String receiverUsername) { this.receiverUsername = receiverUsername; }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                ", senderUsername='" + senderUsername + '\'' +
                ", receiverUsername='" + receiverUsername + '\'' +
                '}';
    }
}
