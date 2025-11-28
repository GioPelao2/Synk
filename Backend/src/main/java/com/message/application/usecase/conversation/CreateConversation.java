package com.message.application.usecase.conversation;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.UserId;

public class CreateConversation {
    private final ConversationRepository conversationRepository;

    public CreateConversation(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation execute(UserId participant1, UserId participant2) {
        if (participant1 == null || participant2 == null) {
            throw new IllegalArgumentException("Participants cannot be null");
        }

        if (participant1.equals(participant2)) {
            throw new IllegalArgumentException("Cannot create conversation with the same user");
        }

        if (conversationRepository.existsBetweenUsers(participant1, participant2)) {
            throw new IllegalArgumentException("Conversation already exists between these users");
        }

        Conversation newConversation = new Conversation(participant1, participant2);
        newConversation.validate();

        return conversationRepository.save(newConversation);
    }
}
