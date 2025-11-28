package com.message.application.usecase.conversation;

import java.util.List;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.UserId;

public class GetUserConversations {
    private final ConversationRepository conversationRepository;

    public GetUserConversations(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public List<Conversation> execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return conversationRepository.findByParticipant(userId);
    }
}
