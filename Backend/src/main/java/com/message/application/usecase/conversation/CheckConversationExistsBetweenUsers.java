package com.message.application.usecase.conversation;

import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.UserId;

public class CheckConversationExistsBetweenUsers {
    private final ConversationRepository conversationRepository;

    public CheckConversationExistsBetweenUsers(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public boolean execute(UserId userId1, UserId userId2) {
        if (userId1 == null || userId2 == null) {
            throw new IllegalArgumentException("UserIds cannot be null");
        }

        if (userId1.equals(userId2)) {
            throw new IllegalArgumentException("Cannot check conversation with the same user");
        }

        return conversationRepository.existsBetweenUsers(userId1, userId2);
    }
}
