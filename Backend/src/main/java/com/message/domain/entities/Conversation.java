package com.message.domain.entities;

import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO: Agregar soporte para conversaciones grupales en el futuro
 * FIXME: El método getLastMessage() puede lanzar IndexOutOfBoundsException
 */
public class Conversation {
    private ConversationId id;
    private List<UserId> participants; // Por ahora solo soporta 1-a-1, añadir grupos?
    private List<Message> messages;
    private LocalDateTime createdAt;

    public Conversation(ConversationId id, List<UserId> participants) {
        this.id = Objects.requireNonNull(id,"ConversationId cannot be null");
        this.participants = Objects.requireNonNull(participants,"Participants cannot be null");
        this.messages = new ArrayList<>(); // Comienza sin mensajes
        this.createdAt = LocalDateTime.now();
    }

    // Agregar un mensaje a la conversación
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    /**
     * Obtiene el último mensaje de la conversación
     * @return el último mensaje
     * @throws IndexOutOfBoundsException si no hay mensajes (debería manejarse mejor)
     */
    public Message getLastMessage() {
        // Esto puede fallar si no hay mensajes pero funciona por ahora
        return messages.get(messages.size() - 1);
    }

    // Verifica si un usuario es parte de esta conversación
    public boolean hasParticipants(UserId userId) {
        return this.participants.contains(userId);
    }
}
