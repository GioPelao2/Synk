package com.message.infrastructure.persistance;

import java.time.LocalDateTime;
import com.message.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "password")
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;
    
    @Column(name = "last_seen", nullable = false)
    private LocalDateTime lastSeen;


    public UserJpaEntity(String username, String email, String password, UserStatus status, LocalDateTime lastSeen) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    public UserJpaEntity(Long id, String username, String email, UserStatus status, LocalDateTime lastSeen) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    //Getters y Setters
    public Long getId() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public UserStatus getStatus() {return status;}
    public LocalDateTime getLastSeen() {return lastSeen;}

    public void setId(Long id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setStatus(UserStatus status) {this.status = status;}
    public void setLastSeen(LocalDateTime lastSeen) {this.lastSeen = lastSeen;}
}
