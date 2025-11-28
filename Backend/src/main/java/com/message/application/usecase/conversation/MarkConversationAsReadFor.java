package com.message.application.usecase.conversation;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;

public class MarkConversationAsReadFor {
    private final ConversationRepository conversationRepository;

    public MarkConversationAsReadFor(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public void execute(ConversationId conversationId, UserId userId) {
        if (conversationId == null || userId == null) {
            throw new IllegalArgumentException("ConversationId and UserId cannot be null");
        }

        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + conversationId));

        if (!conversation.hasParticipant(userId)) {
            throw new IllegalArgumentException("User is not a participant of this conversation");
        }

        conversation.markAllMessagesAsReadFor(userId);
        conversationRepository.save(conversation);
    }
}
