package com.message.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.message.domain.entities.Conversation;
import com.message.domain.entities.Message;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;
import com.message.presentation.mapper.ConversationMapper;
import com.message.presentation.mapper.MessageMapper;

@Repository
public class ConversationRepositoryImpl implements ConversationRepository {

    private final JpaConversationRepository jpaRepository;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;

    @Autowired
    public ConversationRepositoryImpl(JpaConversationRepository jpaRepository,
                                     ConversationMapper conversationMapper,
                                     MessageMapper messageMapper) {
        this.jpaRepository = jpaRepository;
        this.conversationMapper = conversationMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public Conversation save(Conversation conversation) {
        ConversationEntity entity = conversationMapper.toJpaEntity(conversation);
        ConversationEntity saved = jpaRepository.save(entity);
        return conversationMapper.toDomain(saved);
    }

    @Override
    public Optional<Conversation> findById(ConversationId id) {
        return jpaRepository.findById(id.value())
                .map(conversationMapper::toDomain);
    }

    @Override
    public Optional<Conversation> findByParticipants(UserId userId1, UserId userId2) {
        // Intentar buscar en ambos órdenes
        Optional<ConversationEntity> result = jpaRepository.findByTwoParticipants(
            userId1.value(), userId2.value()
        );
        
        if (result.isEmpty()) {
            result = jpaRepository.findByTwoParticipants(userId2.value(), userId1.value());
        }
        
        return result.map(conversationMapper::toDomain);
    }

    @Override
    public List<Conversation> findByParticipant(UserId userId) {
        return jpaRepository.findByParticipantId(userId.value())
                .stream()
                .map(conversationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Conversation> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(conversationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(ConversationId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(ConversationId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public boolean existsBetweenUsers(UserId userId1, UserId userId2) {
        return jpaRepository.existsBetweenTwoUsers(userId1.value(), userId2.value()) ||
               jpaRepository.existsBetweenTwoUsers(userId2.value(), userId1.value());
    }
}
