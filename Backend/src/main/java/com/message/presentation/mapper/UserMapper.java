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
        UserEntity jpaEntity = new UserEntity(
                user.getId().value(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getLastSeen()
        );

        if (!user.getId().isTemporary()) {
            jpaEntity.setId(user.getId().value());
        }

        return jpaEntity;
    }
    public void updateJpaEntity(UserEntity jpaEntity, User domain) {
        jpaEntity.setUsername(domain.getUsername());
        jpaEntity.setEmail(domain.getEmail());
        jpaEntity.setStatus(domain.getStatus());
        jpaEntity.setLastSeen(domain.getLastSeen());
    }

}
