package com.message.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.message.domain.valueobjects.UserId;
import com.message.domain.entities.Message;
import com.message.domain.valueobjects.MessageId;

public interface MessageRepository {

    public Message save(Message message);

    public Optional<Message> findById(MessageId id);

    public List<Message> findConversationHistory(UserId id1, UserId id2);
}
