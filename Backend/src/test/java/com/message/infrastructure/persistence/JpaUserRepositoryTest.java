package com.message.infrastructure.persistence;

import com.message.domain.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Configura una BD en memoria (H2) y escanea repositorios JPA
class JpaUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Test
    @DisplayName("findOnlineUsers: Solo debe retornar usuarios con status ONLINE")
    void findOnlineUsers_ReturnsOnlyOnline() {
        // Arrange: Persistir usuarios en diferentes estados
        createAndPersistUser("user1", "u1@mail.com", UserStatus.ONLINE);
        createAndPersistUser("user2", "u2@mail.com", UserStatus.OFFLINE);
        createAndPersistUser("user3", "u3@mail.com", UserStatus.AWAY);
        createAndPersistUser("user4", "u4@mail.com", UserStatus.ONLINE);

        // Act
        List<UserEntity> onlineUsers = jpaUserRepository.findOnlineUsers();

        // Assert
        assertThat(onlineUsers).hasSize(2);
        assertThat(onlineUsers).extracting(UserEntity::getUsername)
                .containsExactlyInAnyOrder("user1", "user4");
    }

    @Test
    @DisplayName("findAvailableUsers: Debe retornar usuarios ONLINE y AWAY")
    void findAvailableUsers_ReturnsOnlineAndAway() {
        // Arrange
        createAndPersistUser("online", "on@mail.com", UserStatus.ONLINE);
        createAndPersistUser("offline", "off@mail.com", UserStatus.OFFLINE);
        createAndPersistUser("away", "away@mail.com", UserStatus.AWAY);

        // Act
        List<UserEntity> availableUsers = jpaUserRepository.findAvailableUsers();

        // Assert
        assertThat(availableUsers).hasSize(2); // Online + Away
        assertThat(availableUsers).extracting(UserEntity::getUsername)
                .containsExactlyInAnyOrder("online", "away");
        assertThat(availableUsers).extracting(UserEntity::getUsername)
                .doesNotContain("offline");
    }

    @Test
    @DisplayName("existsByUsername: Retorna true si existe, false si no")
    void existsByUsername_WorksCorrectly() {
        createAndPersistUser("existe", "ex@mail.com", UserStatus.OFFLINE);

        boolean exists = jpaUserRepository.existsByUsername("existe");
        boolean notExists = jpaUserRepository.existsByUsername("fantasma");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("findByEmail: Encuentra usuario por email exacto")
    void findByEmail_WorksCorrectly() {
        createAndPersistUser("test", "buscado@mail.com", UserStatus.ONLINE);

        Optional<UserEntity> found = jpaUserRepository.findByEmail("buscado@mail.com");
        Optional<UserEntity> notFound = jpaUserRepository.findByEmail("otro@mail.com");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test");
        assertThat(notFound).isEmpty();
    }

    // Helper para crear datos rápidamente en los tests
    private void createAndPersistUser(String username, String email, UserStatus status) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setStatus(status);
        user.setLastSeen(LocalDateTime.now());
        entityManager.persist(user);
        entityManager.flush();
    }
}