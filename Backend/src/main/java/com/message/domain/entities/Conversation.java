package com.message.domain.entities;

import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * NOTA: Este diseño carga todos los mensajes en memoria.
 * 
 * TODO: Agregar soporte para conversaciones grupales en el futuro
 */
public class Conversation {
    private ConversationId id;
    private List<UserId> participants; // Exactamente 2 usuarios para conversaciones 1-a-1
    private List<Message> messages;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;

    // Constructor para conversaciones desde BD
    public Conversation(ConversationId id, List<UserId> participants, List<Message> messages, LocalDateTime createdAt, LocalDateTime lastMessageAt) {
        this.id = Objects.requireNonNull(id, "ConversationId cannot be null");
        this.participants = Objects.requireNonNull(participants, "Participants cannot be null");
        this.messages = new ArrayList<>(Objects.requireNonNull(messages, "Messages cannot be null"));
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
        this.lastMessageAt = lastMessageAt;
        
        validateParticipants();
    }

    // Constructor para conversaciones nuevas
    public Conversation(UserId participant1, UserId participant2) {
        this.id = ConversationId.from(-1L); // Temporal hasta guardar en BD
        this.participants = new ArrayList<>();
        this.participants.add(Objects.requireNonNull(participant1, "Participant1 cannot be null"));
        this.participants.add(Objects.requireNonNull(participant2, "Participant2 cannot be null"));
        this.messages = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.lastMessageAt = null;
        
        validateParticipants();
    }

    /*
     * Método para asignar ID real después de guardar en BD
     */
    public Conversation withId(ConversationId newId) {
        if (!this.id.value().equals(-1L)) {
            throw new IllegalStateException("Conversation already has an ID assigned");
        }
        return new Conversation(newId, this.participants, this.messages, this.createdAt, this.lastMessageAt);
    }

    public void addMessage(Message message) {
        Objects.requireNonNull(message, "Message cannot be null");
        
        if (!isMessageBetweenParticipants(message)) {
            throw new IllegalArgumentException("Message does not belong to this conversation");
        }
        
        this.messages.add(message);
        this.lastMessageAt = message.getTimestamp();
    }

    public Optional<Message> getLastMessage() {
        if (messages.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(messages.get(messages.size() - 1));
    }

    public boolean hasParticipant(UserId userId) {
        return this.participants.contains(userId);
    }

    public boolean hasBothParticipants(UserId userId1, UserId userId2) {
        return this.participants.contains(userId1) && this.participants.contains(userId2);
    }

    public UserId getOtherParticipant(UserId userId) {
        if (!hasParticipant(userId)) {
            throw new IllegalArgumentException("User is not a participant of this conversation");
        }
        
        return participants.stream()
            .filter(p -> !p.equals(userId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Conversation should have 2 participants"));
    }

    public int getMessageCount() {
        return messages.size();
    }

    public int getUnreadCountFor(UserId userId) {
        if (!hasParticipant(userId)) {
            throw new IllegalArgumentException("User is not a participant of this conversation");
        }
        
        return (int) messages.stream()
            .filter(msg -> msg.isToUser(userId))
            .filter(msg -> !msg.isRead())
            .count();
    }

    public void markAllMessagesAsReadFor(UserId userId) {
        if (!hasParticipant(userId)) {
            throw new IllegalArgumentException("User is not a participant of this conversation");
        }
        
        messages.stream()
            .filter(msg -> msg.isToUser(userId))
            .filter(msg -> !msg.isRead())
            .forEach(Message::markAsRead);
    }

    private boolean isMessageBetweenParticipants(Message message) {
        UserId sender = message.getSenderId();
        UserId receiver = message.getReceiverId();
        
        return (hasParticipant(sender) && hasParticipant(receiver));
    }

    private void validateParticipants() {
        if (participants.size() != 2) {
            throw new IllegalArgumentException("Conversation must have exactly 2 participants");
        }
        
        if (participants.get(0).equals(participants.get(1))) {
            throw new IllegalArgumentException("Participants must be different users");
        }
    }

    public void validate() {
        if (id == null) {
            throw new IllegalArgumentException("ConversationId cannot be null");
        }
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("Participants cannot be null or empty");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt cannot be null");
        }
        
        validateParticipants();
        
        if (createdAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("CreatedAt cannot be in the future");
        }
        
        if (lastMessageAt != null && lastMessageAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("LastMessageAt cannot be before CreatedAt");
        }
        
        for (Message message : messages) {
            if (!isMessageBetweenParticipants(message)) {
                throw new IllegalArgumentException("All messages must be between the conversation participants");
            }
        }
    }

    public ConversationId getId() { return id; }
    public List<UserId> getParticipants() { 
        return Collections.unmodifiableList(participants); 
    }
    
    public List<Message> getMessages() { 
        return Collections.unmodifiableList(messages); 
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastMessageAt() { return lastMessageAt; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Conversation that = (Conversation) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", participants=" + participants +
                ", messageCount=" + messages.size() +
                ", createdAt=" + createdAt +
                ", lastMessageAt=" + lastMessageAt +
                '}';
    }
}
