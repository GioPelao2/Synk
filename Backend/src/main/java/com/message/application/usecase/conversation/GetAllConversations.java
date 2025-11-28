package com.message.application.usecase.conversation;

import java.util.List;

import com.message.domain.entities.Conversation;
import com.message.domain.repositories.ConversationRepository;

public class GetAllConversations {
    private final ConversationRepository conversationRepository;

    public GetAllConversations(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public List<Conversation> execute() {
        return conversationRepository.findAll();
    }
}
