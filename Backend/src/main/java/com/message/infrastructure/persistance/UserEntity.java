package com.message.infrastructure.persistance;

import java.time.LocalDateTime;
import com.message.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
    
    @Column(name = "last_seen")
    private LocalDateTime lastSeen;


    public UserEntity(String username, String email, String password, UserStatus status, LocalDateTime lastSeen) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.lastSeen = lastSeen;
    }
}
