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
    
    public User(UserId id, String username, String email) {
        this.id = Objects.requireNonNull(id,"UserId cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.status = status.OFFLINE;
    }


    public void goOnline() {
        this.status = UserStatus.ONLINE;
        this.lastSeen = LocalDateTime.now();
    }

    public void goOfline() {
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }

    public boolean canReceiveMessage(UserId senderId) {
        //No se puede recibir mensajes de uno mismo
        if (this.id.equals(senderId)) {
            return false;
        }
        return this.status == UserStatus.ONLINE;
    }

    public boolean isOnline() {
        return this.status == UserStatus.ONLINE;
    }

    public UserId getId() {
        return id;
    }

    public UserStatus getStatus() {
        return status;
    }

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
