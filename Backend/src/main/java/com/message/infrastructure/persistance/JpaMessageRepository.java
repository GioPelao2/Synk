package com.message.infrastructure.persistance;

import java.util.List;
import java.util.Optional;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaMessageRepository implements MessageRepository, CrudRepository{
    
    @Override
    public Message save(Message message) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Message> findById(MessageId id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<Message> findConversationHistory(UserId id1, UserId id2) {
        // TODO Auto-generated method stub
        return null;
    }

     @Override
    public Iterable saveAll(Iterable entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Object entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteAll(Iterable entities) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deleteById(Object id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object save(Object entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional findById(Object id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteAllById(Iterable ids) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean existsById(Object id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterable findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable findAllById(Iterable ids) {
        // TODO Auto-generated method stub
        return null;
    }



}
