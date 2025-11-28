package com.message.application.usecase.message;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;

public class CheckIfMessageIsToUser {
    private final MessageRepository messageRepository;

    public CheckIfMessageIsToUser(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public boolean execute(MessageId messageId, UserId userId) {
        if (messageId == null || userId == null) {
            throw new IllegalArgumentException("MessageId and UserId cannot be null");
        }

        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));

        return message.isToUser(userId);
    }
}
