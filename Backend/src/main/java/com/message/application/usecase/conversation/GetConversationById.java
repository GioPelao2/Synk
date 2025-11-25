package com.message.application.usecase.conversation;

import java.util.Optional;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.ConversationId;

public class GetConversationById {
    private final ConversationRepository conversationRepository;

    public GetConversationById(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Optional<Conversation> execute(ConversationId conversationId) {
        if (conversationId == null) {
            throw new IllegalArgumentException("ConversationId cannot be null");
        }
        return conversationRepository.findById(conversationId);
    }
}
