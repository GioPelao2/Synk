package com.message.presentation.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.message.domain.entities.Conversation;
import com.message.domain.entities.Message;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.ConversationEntity;
import com.message.infrastructure.persistence.MessageEntity;
import com.message.infrastructure.mapper.MessageEntityMapper;

@Component
public class ConversationMapper {

    private final MessageEntityMapper messageMapper;

    @Autowired
    public ConversationMapper(MessageEntityMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    /**
     * Convierte ConversationEntity (JPA) a Conversation (Domain)
     */
    public Conversation toDomain(ConversationEntity entity) {
        if (entity == null) return null;

        List<UserId> participants = entity.getParticipantIds().stream()
                .map(UserId::from)
                .collect(Collectors.toList());

        List<Message> messages = entity.getMessages().stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());

        return new Conversation(
                ConversationId.from(entity.getId()),
                participants,
                messages,
                entity.getCreatedAt(),
                entity.getLastMessageAt()
        );
    }

    /**
     * Convierte Conversation (Domain) a ConversationEntity (JPA)
     */
    public ConversationEntity toJpaEntity(Conversation conversation) {
        if (conversation == null) return null;

        List<Long> participantIds = conversation.getParticipants().stream()
                .map(UserId::value)
                .collect(Collectors.toList());

        List<MessageEntity> messageEntities = conversation.getMessages().stream()
                .map(messageMapper::toEntity)
                .collect(Collectors.toList());

        ConversationEntity entity = new ConversationEntity(
                conversation.getId().isTemporary() ? null : conversation.getId().value(),
                participantIds,
                messageEntities,
                conversation.getCreatedAt(),
                conversation.getLastMessageAt()
        );

        return entity;
    }
}
