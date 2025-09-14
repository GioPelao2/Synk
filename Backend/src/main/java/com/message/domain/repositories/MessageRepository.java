package com.message.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.message.domain.valueobjects.UserId;
import com.message.domain.entities.Message;
import com.message.domain.valueobjects.MessageId;

public interface MessageRepository {

    public Message save(Message message);

    public Optional<Message> findById(MessageId id);
 /**
     * Obtiene el historial de mensajes entre dos usuarios
     * FIXME: Esto puede ser lento para conversaciones muy largas
     * TODO: Implementar paginación y ordenamiento por fecha
     */
    public List<Message> findConversationHistory(UserId id1, UserId id2);
}
