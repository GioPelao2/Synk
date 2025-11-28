package com.message.application.usecase.conversation;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;

public class CheckIfUserIsParticipant {
    private final ConversationRepository conversationRepository;

    public CheckIfUserIsParticipant(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public boolean execute(ConversationId conversationId, UserId userId) {
        if (conversationId == null || userId == null) {
            throw new IllegalArgumentException("ConversationId and UserId cannot be null");
        }

        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + conversationId));

        return conversation.hasParticipant(userId);
    }
}

