package com.message.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;

public interface UserRepository {

    public User saveUser(User user);
    public Optional<User> findById(UserId id);

    // Para mostrar usuarios conectados sirve para el UserId
    // TODO: Juako
    public List<User> findOnlineUsers();

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    // Validaciones para evitar duplicados
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
     /**
     * Usuarios disponibles para recibir mensajes (ONLINE + AWAY)
     * TODO: Considerar si incluir usuarios OFFLINE también
     */
    List<User> findAvailableUsers();
}
