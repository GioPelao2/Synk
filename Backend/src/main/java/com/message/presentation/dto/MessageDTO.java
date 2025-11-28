package com.message.presentation.dto;

import com.message.domain.entities.Message;
import java.time.LocalDateTime;

public class MessageDTO {
    
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    public MessageDTO() {}

    // Constructor completo
    public MessageDTO(Long id, Long senderId, Long receiverId, String content,
                     LocalDateTime timestamp, boolean isRead) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public MessageDTO(Message message) {
        // Manejo seguro de nulos para ID temporal (-1) o ID real
        this.id = message.getMessageId() != null ? message.getMessageId().value() : null;
        this.senderId = message.getSenderId().value();
        this.receiverId = message.getReceiverId().value();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
        this.isRead = message.isRead();
    }

    public static MessageDTO forResponse(Long id, Long senderId, Long receiverId,
                                        String content, LocalDateTime timestamp, boolean isRead) {
        return new MessageDTO(id, senderId, receiverId, content, timestamp, isRead);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }

    public String getReceiverUsername() { return receiverUsername; }
    public void setReceiverUsername(String receiverUsername) { this.receiverUsername = receiverUsername; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                '}';
    }
}
