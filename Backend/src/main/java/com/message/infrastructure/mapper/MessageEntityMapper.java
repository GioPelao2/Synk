package com.message.infrastructure.mapper;

import org.springframework.stereotype.Component;
import com.message.domain.entities.Message;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.MessageEntity;

/*
 * Mapper para convertir entre Message (Domain) y MessageEntity (Infrastructure/JPA)
 * Este mapper pertenece a Infrastructure porque conoce detalles de persistencia
 */
@Component
public class MessageEntityMapper {

    /*
     * Convierte MessageEntity (JPA) a Message (Domain)
     */
    public Message toDomain(MessageEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Message(
            MessageId.from(entity.getId()),
            UserId.from(entity.getSenderId()),
            UserId.from(entity.getReceiverId()),
            entity.getMessage(),
            entity.getTimestamp(),
            entity.isRead()
        );
    }

    /*
     * Convierte Message (Domain) a MessageEntity (JPA)
     */
    public MessageEntity toEntity(Message message) {
        if (message == null) {
            return null;
        }

        // Si el mensaje tiene un ID temporal (-1), no lo incluimos
        if (message.getMessageId().value().equals(-1L)) {
            MessageEntity entity = new MessageEntity(
                message.getContent(),
                message.getSenderId().value(),
                message.getReceiverId().value()
            );
            entity.setTimestamp(message.getTimestamp());
            entity.setRead(message.isRead());
            return entity;
        } else {
            return new MessageEntity(
                message.getMessageId().value(),
                message.getContent(),
                message.getSenderId().value(),
                message.getReceiverId().value(),
                message.getTimestamp(),
                message.isRead()
            );
        }
    }

    /*
     * Actualiza una entidad existente con datos del dominio
     * Útil para operaciones de actualización
     */
    public void updateEntity(MessageEntity entity, Message message) {
        if (entity == null || message == null) {
            return;
        }
        
        entity.setMessage(message.getContent());
        entity.setSenderId(message.getSenderId().value());
        entity.setReceiverId(message.getReceiverId().value());
        entity.setTimestamp(message.getTimestamp());
        entity.setRead(message.isRead());
    }
}
