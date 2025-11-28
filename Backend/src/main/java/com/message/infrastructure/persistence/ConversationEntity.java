package com.message.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "conversations")
public class ConversationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "conversation_participants", 
                    joinColumns = @JoinColumn(name = "conversation_id"))
    @Column(name = "user_id")
    private List<Long> participantIds = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "conversation_id")
    @OrderBy("timestamp ASC")
    private List<MessageEntity> messages = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    public ConversationEntity() {}

    public ConversationEntity(Long id, List<Long> participantIds, List<MessageEntity> messages, 
                             LocalDateTime createdAt, LocalDateTime lastMessageAt) {
        this.id = id;
        this.participantIds = participantIds != null ? new ArrayList<>(participantIds) : new ArrayList<>();
        this.messages = messages != null ? new ArrayList<>(messages) : new ArrayList<>();
        this.createdAt = createdAt;
        this.lastMessageAt = lastMessageAt;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<Long> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<Long> participantIds) { this.participantIds = participantIds; }

    public List<MessageEntity> getMessages() { return messages; }
    public void setMessages(List<MessageEntity> messages) { this.messages = messages; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(LocalDateTime lastMessageAt) { this.lastMessageAt = lastMessageAt; }
}
