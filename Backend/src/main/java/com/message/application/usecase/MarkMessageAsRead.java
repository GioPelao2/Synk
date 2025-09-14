package com.message.application.usecase;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.MessageId;

public class MarkMessageAsRead {
    private MessageRepository messageRepository;

    // Constructor para inyección de dependencias
    public MarkMessageAsRead(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void execute(MessageId messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));

        message.markAsRead();

        messageRepository.save(message);
    }
}
