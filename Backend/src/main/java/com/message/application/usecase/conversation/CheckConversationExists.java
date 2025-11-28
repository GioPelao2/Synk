package com.message.application.usecase.conversation;

import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.ConversationId;

public class CheckConversationExists {
    private final ConversationRepository conversationRepository;

    public CheckConversationExists(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public boolean execute(ConversationId conversationId) {
        if (conversationId == null) {
            throw new IllegalArgumentException("ConversationId cannot be null");
        }
        return conversationRepository.existsById(conversationId);
    }
}

