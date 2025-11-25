package com.message.presentation.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    public MessageDTO() {}

    public MessageDTO(Long id, Long senderId, Long receiverId, String content,
                     LocalDateTime timestamp, boolean isRead) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
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
