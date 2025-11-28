package com.message.presentation.mapper;

import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.UserEntity;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity jpaEntity) {
        if (jpaEntity == null) return null;
        return new User(
                UserId.from(jpaEntity.getId()),
                jpaEntity.getUsername(),
                jpaEntity.getEmail(),
                jpaEntity.getStatus(),
                jpaEntity.getLastSeen()
        );
    }

    public UserEntity toJpaEntity(User user) {
        if (user == null) return null;
        
        // Si el usuario tiene ID temporal, no lo asignes (déjalo null para que JPA lo genere)
        if (user.getId().isTemporary()) {
            return new UserEntity(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getStatus(),
                    user.getLastSeen()
            );
        } else {
            // Si tiene ID real, úsalo
            return new UserEntity(
                    user.getId().value(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getStatus(),
                    user.getLastSeen()
            );
        }
    }

    public void updateJpaEntity(UserEntity jpaEntity, User domain) {
        jpaEntity.setUsername(domain.getUsername());
        jpaEntity.setEmail(domain.getEmail());
        jpaEntity.setStatus(domain.getStatus());
        jpaEntity.setLastSeen(domain.getLastSeen());
        // No actualices el password aquí, eso debe ser en un método separado por seguridad
    }
}