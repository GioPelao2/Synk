package com.message.applicationservices;

import java.util.List;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.UserId;

public class GetConversationHistory {
    MessageRepository repository;

    public List<Message> execute(UserId userId, UserId userId2) {
        return repository.findConversationHistory(userId, userId2);
    }
}
