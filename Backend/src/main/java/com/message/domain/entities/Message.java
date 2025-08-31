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
    private MessageType messageIdType;
    private MessageStatus status;

    public Message(MessageId messageId, UserId senderId, UserId receiverId, String content) {
        this.messageId = messageId;
        this.senderId = Objects.requireNonNull(senderId,"SenderId cannot be null");
        this.receiverId = Objects.requireNonNull(receiverId,"ReceiverId cannot be null");
        this.content = Objects.requireNonNull(content,"Content cannot be null");
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.SENT;
    }

    public void markAsRead() {
        this.status = MessageStatus.READ;
    }

    public void validate() {
        if (this.messageId == null) {
            throw new IllegalArgumentException("MessageId cannot be null");
        }
        if (this.senderId == null) {
            throw new IllegalArgumentException("SenderId cannot be null");
        }
        if (this.receiverId == null) {
            throw new IllegalArgumentException("ReceiverId cannot be null");
        }
        if (this.content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
    }

    public boolean isFromUser(UserId userId) {
        return this.senderId.equals(userId);
    }
}
