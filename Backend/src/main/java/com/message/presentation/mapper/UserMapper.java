package com.message.presentation.mapper;

import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.UserEntity;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Convierte UserEntity (JPA) a User (Domain)
     * Incluye el passwordHash para operaciones completas
     */
    public User toDomain(UserEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        return new User(
            UserId.from(jpaEntity.getId()),
            jpaEntity.getUsername(),
            jpaEntity.getEmail(),
            jpaEntity.getPasswordHash(),
            jpaEntity.getStatus(),
            jpaEntity.getLastSeen()
        );
    }

    /**
     * Convierte User (Domain) a UserEntity (JPA)
     */
    public UserEntity toJpaEntity(User user) {
        if (user == null) return null;
        
        // Si el usuario tiene ID temporal, no lo asigna ( null para que JPA lo genere)
        if (user.getId().isTemporary()) {
            return new UserEntity(
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getStatus(),
                user.getLastSeen()
            );
        } else {
            // Si tiene ID real, lo usa
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

    /**
     * Actualiza una entidad JPA existente con datos del dominio
     */
    public void updateJpaEntity(UserEntity jpaEntity, User domain) {
        if (jpaEntity == null || domain == null) return;
        
        jpaEntity.setUsername(domain.getUsername());
        jpaEntity.setEmail(domain.getEmail());
        jpaEntity.setStatus(domain.getStatus());
        jpaEntity.setLastSeen(domain.getLastSeen());
        // TODO:NO actualiza passwordHash - debe ser en un método separado por seguridad
    }
}
