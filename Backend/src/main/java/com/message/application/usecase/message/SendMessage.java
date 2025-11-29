package com.message.application.usecase.message;

import com.message.application.usecase.conversation.GetOrCreateConversation;
import com.message.domain.entities.Conversation;
import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;

public class SendMessage {
    private final MessageRepository messageRepository;
    private final GetOrCreateConversation getOrCreateConversation;

    public SendMessage(MessageRepository messageRepository, GetOrCreateConversation GetOrCreateConversation) {
        this.messageRepository = messageRepository;
        this.getOrCreateConversation = GetOrCreateConversation;
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

        Conversation conversation = getOrCreateConversation.execute(senderId, receiverId);

        ConversationId conversationId = conversation.getId();

        Message newMessage = new Message(conversationId, senderId, receiverId, content);
        
        newMessage.validate();
        
        return messageRepository.save(newMessage);
    }
}
