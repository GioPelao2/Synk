package com.message.domain.entities;

import com.message.domain.valueobjects.UserId;
import com.message.domain.enums.UserStatus;
import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private UserId id;
    private String username;
    private String email;
    private String passwordHash;
    private UserStatus status;
    private LocalDateTime lastSeen;

    // Constructor para nuevos usuarios (registro) - con password
    public User(String username, String email, String passwordHash) {
        this.id = UserId.temporary();
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password cannot be null");
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }
    
    // Constructor COMPLETO para usuarios desde BD (con passwordHash)
    public User(UserId id, String username, String email, String passwordHash, 
                UserStatus status, LocalDateTime lastSeen) {
        this.id = Objects.requireNonNull(id, "UserId cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "PasswordHash cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.lastSeen = Objects.requireNonNull(lastSeen, "LastSeen cannot be null");
    }

    // Constructor para usuarios desde BD (sin passwordHash para DTOs/vistas)
    public User(UserId id, String username, String email, UserStatus status, LocalDateTime lastSeen) {
        this.id = Objects.requireNonNull(id, "UserId cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.passwordHash = null; // Opcional para casos donde no se necesita el hash
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.lastSeen = Objects.requireNonNull(lastSeen, "LastSeen cannot be null");
    }

    /*
     * Método para asignar ID real después de guardar en BD
     */
    public User withId(UserId newId) {
        if (!this.id.isTemporary()) {
            throw new IllegalStateException("User already has an ID assigned");
        }
        return new User(newId, this.username, this.email, this.passwordHash, 
                       this.status, this.lastSeen);
    }

    public void goOnline() {
        this.status = UserStatus.ONLINE;
        this.lastSeen = LocalDateTime.now();
    }

    public void goOffline() {
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }

    public void goAway() {
        this.status = UserStatus.AWAY;
        this.lastSeen = LocalDateTime.now();
    }

    public boolean canReceiveMessage(UserId senderId) {
        if (this.id.equals(senderId)) {
            return false;
        }
        return this.status == UserStatus.ONLINE || this.status == UserStatus.AWAY;
    }

    public boolean isOnline() {
        return this.status == UserStatus.ONLINE;
    }

    public boolean isAway() {
        return this.status == UserStatus.AWAY;
    }

    public boolean isOffline() {
        return this.status == UserStatus.OFFLINE;
    }

    public UserId getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public UserStatus getStatus() { return status; }
    public LocalDateTime getLastSeen() { return lastSeen; }
    public String getPasswordHash() { return passwordHash; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
