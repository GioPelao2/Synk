package com.message.domain.entities;

import com.message.domain.valueobjects.UserId;
import com.message.domain.enums.UserStatus;
import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private UserId id;
    private String username;
    private String email;
    private UserStatus status;
    private LocalDateTime lastSeen;


    //Constructor para usuarios existentes
    public User(UserId id, String username, String email, UserStatus status, LocalDateTime lastSeen) {
        this.id = Objects.requireNonNull(id, "UserId cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }

    //Constructor para usuarios nuevos
    public User(String username, String email) {
        this.id = UserId.newId();
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }

    public User withId(UserId newId) {
        if (!this.id.isTemporary()) {
            throw new IllegalStateException("User already has an ID assigned");
        }
        return new User(newId, this.username, this.email, this.status, this.lastSeen);
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

    public void changeStatus(UserStatus newStatus) {
        Objects.requireNonNull(newStatus, "UserStatus cannot be null");
        this.status = newStatus;
        this.lastSeen = LocalDateTime.now();
    }

    public boolean canReceiveMessage(UserId senderId) {
        // No se puede recibir mensajes de uno mismo
        if (this.id.equals(senderId)) {
            return false;
        }
        // Solo usuarios ONLINE y AWAY pueden recibir mensajes inmediatamente
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

    //Getters
    public UserId getId() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public UserStatus getStatus() {return status;}
    public LocalDateTime getLastSeen() {return lastSeen;}

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
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
