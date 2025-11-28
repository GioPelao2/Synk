package com.message.infrastructure.persistence;

import java.time.LocalDateTime;
import com.message.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "last_seen", nullable = false)
    private LocalDateTime lastSeen;

    // Constructor vacío (requerido por JPA)
    public UserEntity() {
    }

    // Constructor sin ID (para nuevos usuarios)
    public UserEntity(String username, String email, String passwordHash, UserStatus status, LocalDateTime lastSeen) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    // Constructor con ID (para usuarios existentes)
    public UserEntity(Long id, String username, String email, String passwordHash, UserStatus status, LocalDateTime lastSeen) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UserStatus getStatus() { return status; }
    public LocalDateTime getLastSeen() { return lastSeen; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setStatus(UserStatus status) { this.status = status; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }
}