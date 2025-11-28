package com.message.infrastructure.persistence;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import com.message.presentation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserRepositoryImpl(JpaUserRepository jpaRepository, UserMapper userMapper) {
        this.jpaRepository = jpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User saveUser(User user) {
        var jpaEntity = userMapper.toJpaEntity(user);
        var savedEntity = jpaRepository.save(jpaEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue())
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UserId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsById(UserId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findOnlineUsers() {
        return jpaRepository.findOnlineUsers()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAvailableUsers() {
        return jpaRepository.findAvailableUsers()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findOfflineUsers() {
        return jpaRepository.findOfflineUsers()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAwayUsers() {
        return jpaRepository.findAwayUsers()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
}
