package com.message.infrastructure.persistance;

import java.util.List;
import java.util.Optional;
import com.message.domain.entities.User;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UserId>, UserRepository {
    
    @Override
    @Query("SELECT u FROM User u WHERE u.isOnline = true")
    List<User> findOnlineUsers();
    
    @Override
    Optional<User> findById(UserId id);
    
    @Override
    default User saveUser(User user) {
        return save(user);
    }
}
