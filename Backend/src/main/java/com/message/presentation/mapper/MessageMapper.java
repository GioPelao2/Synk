package com.message.presentation.mapper;

import org.springframework.stereotype.Component;
import com.message.domain.entities.Message;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.MessageEntity;

@Component
public class MessageMapper {

    public Message toDomain(MessageEntity jpaEntity) {
        if (jpaEntity == null) return null;

        return new Message(
            MessageId.from(jpaEntity.getId()),
            UserId.from(jpaEntity.getSenderId()),
            UserId.from(jpaEntity.getReceiverId()),
            jpaEntity.getMessage()
        );
    }

    public MessageEntity toJpaEntity(Message message) {
        if (message == null) return null;

        MessageEntity jpaEntity;

        // Si el mensaje tiene un ID temporal (-1), no lo incluimos
        if (message.getMessageId().value().equals(-1L)) {
            jpaEntity = new MessageEntity(
                message.getContent(),
                message.getSenderId().value(),
                message.getReceiverId().value()
            );
        } else {
            jpaEntity = new MessageEntity(
                message.getMessageId().value(),
                message.getContent(),
                message.getSenderId().value(),
                message.getReceiverId().value()
            );
        }

        return jpaEntity;
    }

    public void updateJpaEntity(MessageEntity jpaEntity, Message domain) {
        jpaEntity.setMessage(domain.getContent());
        jpaEntity.setSenderId(domain.getSenderId().value());
        jpaEntity.setReceiverId(domain.getReceiverId().value());
    }
}
