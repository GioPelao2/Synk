package com.message.application.usecase.conversation;

import com.message.domain.entities.Conversation;
import com.message.domain.entities.Message;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;

public class AddMessageToConversation {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public AddMessageToConversation(ConversationRepository conversationRepository, 
                                   MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    public Conversation execute(ConversationId conversationId, UserId senderId, 
                               UserId receiverId, String content) {
        if (conversationId == null) {
            throw new IllegalArgumentException("ConversationId cannot be null");
        }

        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + conversationId));

        if (!conversation.hasBothParticipants(senderId, receiverId)) {
            throw new IllegalArgumentException("Users are not participants of this conversation");
        }

        Message newMessage = new Message(senderId, receiverId, content);
        newMessage.validate();
        Message savedMessage = messageRepository.save(newMessage);

        conversation.addMessage(savedMessage);

        return conversationRepository.save(conversation);
    }
}
