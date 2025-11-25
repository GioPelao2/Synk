package com.message.application.usecase.message;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.MessageId;

public class ValidateMessage {
    private final MessageRepository messageRepository;

    public ValidateMessage(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public boolean execute(MessageId messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("MessageId cannot be null");
        }

        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));

        try {
            message.validate();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
