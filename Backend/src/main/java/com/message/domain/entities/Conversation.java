package com.message.domain.entities;

import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Conversation {
    private ConversationId id;
    private List<UserId> participants;
    private List<Message> messages;
    private LocalDateTime createdAt;

    public Conversation(ConversationId id, List<UserId> participants) {
        this.id = Objects.requireNonNull(id,"ConversationId cannot be null");
        this.participants = Objects.requireNonNull(participants,"Participants cannot be null");
        this.messages = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }


    public Message getLastMessage() {
        return messages.get(messages.size() - 1);
    }

    public boolean hasParticipants(UserId userId) {
        return this.participants.contains(userId);
    }
}
