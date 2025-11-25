package com.message.application.usecase.message;

import java.util.List;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.UserId;

public class MarkAllMessagesAsRead {
    private final MessageRepository messageRepository;

    public MarkAllMessagesAsRead(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void execute(UserId receiverId, UserId senderId) {
        if (receiverId == null || senderId == null) {
            throw new IllegalArgumentException("UserIds cannot be null");
        }

        List<Message> messages = messageRepository.findConversationHistory(receiverId, senderId);

        messages.stream()
            .filter(message -> message.isToUser(receiverId))
            .filter(message -> !message.isRead())
            .forEach(message -> {
                message.markAsRead();
                messageRepository.save(message);
            });
    }
}
