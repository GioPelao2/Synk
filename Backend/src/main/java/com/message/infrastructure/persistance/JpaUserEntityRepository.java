package com.message.infrastructure.persistance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserEntityRepository extends JpaRepository<UserJpaEntity, Long>{
    @Query("SELECT u FROM UserJpaEntity u WHERE u.status = 'ONLINE'")
    List<UserJpaEntity> findOnlineUsers();

    @Query("SELECT u FROM UserJpaEntity u WHERE u.status in ('ONLINE', 'AWAY')")
    List<UserJpaEntity> findAvailableUsers();

    Optional<UserJpaEntity> findByUsername(String username);

    Optional<UserJpaEntity> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM UserJpaEntity u WHERE u.username = :username")
    boolean existsByUsername(String username);

    @Query("SELECT COUNT(u) > 0 FROM UserJpaEntity u WHERE u.email = :email")
    boolean existsByEmail(String email);
}
