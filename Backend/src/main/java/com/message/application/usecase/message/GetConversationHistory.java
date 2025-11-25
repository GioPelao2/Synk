package com.message.application.usecase.message;

import java.util.List;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.UserId;

public class GetConversationHistory {
    private final MessageRepository messageRepository;

    public GetConversationHistory(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> execute(UserId userId1, UserId userId2) {
        if (userId1 == null || userId2 == null) {
            throw new IllegalArgumentException("UserIds cannot be null");
        }

        if (userId1.equals(userId2)) {
            throw new IllegalArgumentException("Cannot get conversation history with the same user");
        }

        return messageRepository.findConversationHistory(userId1, userId2);
    }
}
