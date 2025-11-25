package com.message.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.entities.Message;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.mapper.MessageEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private final JpaMessageRepository jpaRepository;
    private final MessageEntityMapper messageMapper;

    @Autowired
    public MessageRepositoryImpl(JpaMessageRepository jpaRepository, MessageEntityMapper messageMapper) {
        this.jpaRepository = jpaRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public Message save(Message message) {
        MessageEntity jpaEntity = messageMapper.toEntity(message);
        MessageEntity savedEntity = jpaRepository.save(jpaEntity);
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

    @Override
    public Optional<Message> findLastMessageBetweenUsers(UserId id1, UserId id2) {
        return jpaRepository.findLastMessageBetweenUsers(id1.value(), id2.value())
                .map(messageMapper::toDomain);
    }

    @Override
    public List<UserId> findConversationPartners(UserId userId) {
        return jpaRepository.findConversationPartners(userId.value())
                .stream()
                .map(UserId::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findUnreadMessagesByReceiver(UserId receiverId) {
        return jpaRepository.findUnreadMessagesByUserId(receiverId.value())
                .stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findMessagesBySender(UserId senderId) {
        return jpaRepository.findMessagesSentByUser(senderId.value())
                .stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findMessagesByReceiver(UserId receiverId) {
        return jpaRepository.findMessagesReceivedByUser(receiverId.value())
                .stream()
                .map(messageMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countUnreadMessagesByReceiver(UserId receiverId) {
        return jpaRepository.countUnreadMessages(receiverId.value());
    }
}
