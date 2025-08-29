package com.mesagge.domain.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Conversation {
    private ConversationId id;
    private List<UserId> participants;
    private List<Message> messages;
    private LocalDateTime createdAt;


    public void addMessage(Message message) {
        //TODO
    }

    public Message getLastMessage() {
        //TODO:
    }

    //TODO: Hacer el constructor
}
