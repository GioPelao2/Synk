package com.message.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.entities.Message;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.presentation.mapper.MessageMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//FIXME: Arreglar los masppers (crearlos)
@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private final JpaMessageRepository jpaRepository;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageRepositoryImpl(JpaMessageRepository jpaRepository, MessageMapper messageMapper) {
        this.jpaRepository = jpaRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public Message save(Message message) {
        var jpaEntity = messageMapper.toJpaEntity(message);
        var savedEntity = jpaRepository.save(jpaEntity);
        return messageMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Message> findById(MessageId id) {
        return jpaRepository.findById(id.value())
                .map(messageMapper::toDomain);
    }

    @Override
    public List<Message> findConversationHistory(UserId id1, UserId id2) {
        return jpaRepository.findConversationHistory(id1.value(), id2.value())
                .stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }

    // Métodos adicionales útiles
    public List<Message> findUnreadMessagesByUserId(UserId userId) {
        return jpaRepository.findUnreadMessagesByUserId(userId.value())
                .stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<UserId> findConversationPartners(UserId userId) {
        return jpaRepository.findConversationPartners(userId.value())
                .stream()
                .map(UserId::from)
                .collect(Collectors.toList());
    }

    public List<Message> findMessagesSentByUser(UserId userId) {
        return jpaRepository.findMessagesSentByUser(userId.value())
                .stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Message> findMessagesReceivedByUser(UserId userId) {
        return jpaRepository.findMessagesReceivedByUser(userId.value())
                .stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Message> findLastMessageBetweenUsers(UserId id1, UserId id2) {
        return jpaRepository.findLastMessageBetweenUsers(id1.value(), id2.value())
                .map(messageMapper::toDomain);
    }

    public Long countUnreadMessages(UserId userId) {
        return jpaRepository.countUnreadMessages(userId.value());
    }

    public void markMessageAsRead(MessageId messageId) {
        jpaRepository.findById(messageId.value())
                .ifPresent(entity -> {
                    entity.setRead(true);
                    jpaRepository.save(entity);
                });
    }

    public void markAllMessagesAsRead(UserId senderId, UserId receiverId) {
        List<MessageEntity> messages = jpaRepository.findConversationHistory(senderId.value(), receiverId.value());
        messages.forEach(message -> {
            if (message.getReceiverId().equals(receiverId.value()) && !message.isRead()) {
                message.setRead(true);
            }
        });
        jpaRepository.saveAll(messages);
    }
}
