package com.message.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;

public interface UserRepository {

    User saveUser(User user);
    
    Optional<User> findById(UserId id);
    
    List<User> findAll();
    
    void deleteById(UserId id);

    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

    boolean existsById(UserId id);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

    List<User> findOnlineUsers();
    
    List<User> findAvailableUsers();
    
    List<User> findOfflineUsers();
    
    List<User> findAwayUsers();
}
