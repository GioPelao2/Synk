package com.message.application.usecase.conversation;

import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.ConversationId;

public class DeleteConversation {
    private final ConversationRepository conversationRepository;

    public DeleteConversation(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public void execute(ConversationId conversationId) {
        if (conversationId == null) {
            throw new IllegalArgumentException("ConversationId cannot be null");
        }

        if (!conversationRepository.existsById(conversationId)) {
            throw new IllegalArgumentException("Conversation not found with id: " + conversationId);
        }

        conversationRepository.deleteById(conversationId);
    }
}

