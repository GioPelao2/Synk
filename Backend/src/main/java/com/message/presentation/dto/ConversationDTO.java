package com.message.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ConversationDTO {
    
    private Long id;
    private List<Long> participantIds;
    private List<MessageDTO> messages;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;
    private Integer messageCount;
    private Integer unreadCount;

    public ConversationDTO() {}

    public ConversationDTO(Long id, List<Long> participantIds, List<MessageDTO> messages,
                          LocalDateTime createdAt, LocalDateTime lastMessageAt) {
        this.id = id;
        this.participantIds = participantIds;
        this.messages = messages;
        this.createdAt = createdAt;
        this.lastMessageAt = lastMessageAt;
        this.messageCount = messages != null ? messages.size() : 0;
    }

    // Constructor simplificado (sin mensajes completos)
    public ConversationDTO(Long id, List<Long> participantIds, LocalDateTime createdAt,
                          LocalDateTime lastMessageAt, Integer messageCount, Integer unreadCount) {
        this.id = id;
        this.participantIds = participantIds;
        this.messages = null;
        this.createdAt = createdAt;
        this.lastMessageAt = lastMessageAt;
        this.messageCount = messageCount;
        this.unreadCount = unreadCount;
    }

    // Método factory para respuestas completas (con mensajes)
    public static ConversationDTO fullResponse(Long id, List<Long> participantIds, 
                                              List<MessageDTO> messages,
                                              LocalDateTime createdAt, 
                                              LocalDateTime lastMessageAt) {
        return new ConversationDTO(id, participantIds, messages, createdAt, lastMessageAt);
    }

    // Método factory para listas (sin mensajes)
    public static ConversationDTO summaryResponse(Long id, List<Long> participantIds,
                                                 LocalDateTime createdAt,
                                                 LocalDateTime lastMessageAt,
                                                 Integer messageCount,
                                                 Integer unreadCount) {
        return new ConversationDTO(id, participantIds, createdAt, lastMessageAt, 
                                  messageCount, unreadCount);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<Long> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<Long> participantIds) { this.participantIds = participantIds; }

    public List<MessageDTO> getMessages() { return messages; }
    public void setMessages(List<MessageDTO> messages) { this.messages = messages; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(LocalDateTime lastMessageAt) { this.lastMessageAt = lastMessageAt; }

    public Integer getMessageCount() { return messageCount; }
    public void setMessageCount(Integer messageCount) { this.messageCount = messageCount; }

    public Integer getUnreadCount() { return unreadCount; }
    public void setUnreadCount(Integer unreadCount) { this.unreadCount = unreadCount; }

    @Override
    public String toString() {
        return "ConversationDTO{" +
                "id=" + id +
                ", participantIds=" + participantIds +
                ", messageCount=" + messageCount +
                ", createdAt=" + createdAt +
                ", lastMessageAt=" + lastMessageAt +
                '}';
    }
}
