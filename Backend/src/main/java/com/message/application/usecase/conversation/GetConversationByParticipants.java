package com.message.application.usecase.conversation;

import java.util.Optional;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.UserId;

public class GetConversationByParticipants {
    private final ConversationRepository conversationRepository;

    public GetConversationByParticipants(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Optional<Conversation> execute(UserId userId1, UserId userId2) {
        if (userId1 == null || userId2 == null) {
            throw new IllegalArgumentException("UserIds cannot be null");
        }

        if (userId1.equals(userId2)) {
            throw new IllegalArgumentException("Cannot get conversation with the same user");
        }

        return conversationRepository.findByParticipants(userId1, userId2);
    }
}
