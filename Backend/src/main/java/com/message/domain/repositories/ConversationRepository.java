package com.message.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.message.domain.entities.Conversation;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;

public interface ConversationRepository {

    Conversation save(Conversation conversation);

    Optional<Conversation> findById(ConversationId id);

    /**
     * Busca la conversación entre dos usuarios específicos
     * @return Optional con la conversación si existe, empty si no
     */
    Optional<Conversation> findByParticipants(UserId userId1, UserId userId2);

    /**
     * Obtiene todas las conversaciones de un usuario
     * @param userId ID del usuario
     * @return Lista de conversaciones donde el usuario es participante
     */
    List<Conversation> findByParticipant(UserId userId);

    List<Conversation> findAll();

    void deleteById(ConversationId id);

    boolean existsById(ConversationId id);

    boolean existsBetweenUsers(UserId userId1, UserId userId2);
}
