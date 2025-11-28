package com.message.application.usecase.message;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.UserId;

public class SendMessage {
    private final MessageRepository messageRepository;

    public SendMessage(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message execute(UserId senderId, UserId receiverId, String content) {
        if (senderId == null) {
            throw new IllegalArgumentException("SenderId cannot be null");
        }
        if (receiverId == null) {
            throw new IllegalArgumentException("ReceiverId cannot be null");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        Message newMessage = new Message(senderId, receiverId, content);
        
        newMessage.validate();
        
        return messageRepository.save(newMessage);
    }
}
