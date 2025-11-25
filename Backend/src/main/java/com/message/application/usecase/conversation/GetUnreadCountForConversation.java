package com.message.application.usecase.conversation;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;

public class GetUnreadCountForConversation {
    private final ConversationRepository conversationRepository;

    public GetUnreadCountForConversation(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public int execute(ConversationId conversationId, UserId userId) {
        if (conversationId == null || userId == null) {
            throw new IllegalArgumentException("ConversationId and UserId cannot be null");
        }

        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + conversationId));

        if (!conversation.hasParticipant(userId)) {
            throw new IllegalArgumentException("User is not a participant of this conversation");
        }

        return conversation.getUnreadCountFor(userId);
    }
}

