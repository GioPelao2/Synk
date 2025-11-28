package com.message.application.usecase.message;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;

public class MarkMessageAsRead {
    private final MessageRepository messageRepository;

    public MarkMessageAsRead(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void execute(MessageId messageId, UserId userId) {
        if (messageId == null) {
            throw new IllegalArgumentException("MessageId cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));

        if (!message.isToUser(userId)) {
            throw new IllegalArgumentException("User is not the receiver of this message");
        }

        message.markAsRead();
        messageRepository.save(message);
    }
}

