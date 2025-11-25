package com.message.presentation.dto;

import com.message.domain.enums.UserStatus;
import java.time.LocalDateTime;

public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private UserStatus status;
    private LocalDateTime lastSeen;

    public UserDTO() {}

    public UserDTO(Long id, String username, String email, UserStatus status, LocalDateTime lastSeen) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    // Constructor para registro (con password, sin id)
    public UserDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Método para crear DTO de respuesta
    public static UserDTO forResponse(Long id, String username, String email, UserStatus status, LocalDateTime lastSeen) {
        return new UserDTO(id, username, email, status, lastSeen);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public LocalDateTime getLastSeen() { return lastSeen; }
    public void setLastSeen(LocalDateTime lastSeen) { this.lastSeen = lastSeen; }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
