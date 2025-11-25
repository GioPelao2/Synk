
package com.message.application.usecase.conversation;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.UserId;

public class GetOrCreateConversation {
    private final ConversationRepository conversationRepository;

    public GetOrCreateConversation(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation execute(UserId participant1, UserId participant2) {
        if (participant1 == null || participant2 == null) {
            throw new IllegalArgumentException("Participants cannot be null");
        }

        if (participant1.equals(participant2)) {
            throw new IllegalArgumentException("Cannot create conversation with the same user");
        }

        return conversationRepository.findByParticipants(participant1, participant2)
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation(participant1, participant2);
                    return conversationRepository.save(newConversation);
                });
    }
}

