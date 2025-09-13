package com.message.infrastructure.persistance;

import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.UserMapper;
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
    public List<User> findOnlineUsers() {
        return jpaRepository.findOnlineUsers()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    // Métodos adicionales útiles
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }

    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    public List<User> findAvailableUsers() {
        return jpaRepository.findAvailableUsers()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
}
