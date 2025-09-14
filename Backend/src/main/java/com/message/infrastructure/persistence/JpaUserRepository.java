package com.message.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    // Encontrar usuarios online
    @Query("SELECT u FROM UserEntity u WHERE u.status = 'ONLINE'")
    List<UserEntity> findOnlineUsers();

    // Encontrar usuarios disponibles (ONLINE y AWAY)
    @Query("SELECT u FROM UserEntity u WHERE u.status IN ('ONLINE', 'AWAY')")
    List<UserEntity> findAvailableUsers();

    // Buscar por username
    Optional<UserEntity> findByUsername(String username);

    // Buscar por email
    Optional<UserEntity> findByEmail(String email);

    // Verificar si existe username
    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

    // Verificar si existe email
    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // Buscar usuarios offline
    @Query("SELECT u FROM UserEntity u WHERE u.status = 'OFFLINE'")
    List<UserEntity> findOfflineUsers();

    // Buscar usuarios away
    @Query("SELECT u FROM UserEntity u WHERE u.status = 'AWAY'")
    List<UserEntity> findAwayUsers();

    // Buscar usuarios por patrón en username
    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:pattern%")
    List<UserEntity> findByUsernameContaining(@Param("pattern") String pattern);

    // Buscar usuarios activos en las últimas X horas
    @Query("SELECT u FROM UserEntity u WHERE u.lastSeen > :since")
    List<UserEntity> findUsersActiveSince(@Param("since") java.time.LocalDateTime since);
}
