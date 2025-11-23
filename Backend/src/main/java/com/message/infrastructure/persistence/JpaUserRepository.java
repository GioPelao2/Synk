package com.message.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.status = 'ONLINE'")
    List<UserEntity> findOnlineUsers();

    @Query("SELECT u FROM UserEntity u WHERE u.status IN ('ONLINE', 'AWAY')")
    List<UserEntity> findAvailableUsers();

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u WHERE u.status = 'OFFLINE'")
    List<UserEntity> findOfflineUsers();

    @Query("SELECT u FROM UserEntity u WHERE u.status = 'AWAY'")
    List<UserEntity> findAwayUsers();

    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:pattern%")
    List<UserEntity> findByUsernameContaining(@Param("pattern") String pattern);

    @Query("SELECT u FROM UserEntity u WHERE u.lastSeen > :since")
    List<UserEntity> findUsersActiveSince(@Param("since") java.time.LocalDateTime since);
}
