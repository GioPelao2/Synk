package com.message.application.usecase.message;

import java.util.Optional;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.MessageId;

public class GetMessageById {
    private final MessageRepository messageRepository;

    public GetMessageById(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Optional<Message> execute(MessageId messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("MessageId cannot be null");
        }
        return messageRepository.findById(messageId);
    }
}
