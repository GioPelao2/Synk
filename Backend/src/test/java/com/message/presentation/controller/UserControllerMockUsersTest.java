package com.message.presentation.controller;

import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.JpaMessageRepository; // Importación nueva
import com.message.infrastructure.persistence.JpaUserRepository;     // Importación nueva
import com.message.infrastructure.persistence.UserRepositoryImpl;
import com.message.application.service.UserService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserControllerMockUsersTest.BypassJpaConfig.class)
class UserControllerMockUsersTest {

    @Autowired
    private MockMvc mockMvc;

    // --- MOCKS PARA DEPENDENCIAS DEL CONTROLADOR ---
    @MockBean
    private UserRepositoryImpl userRepository; // El controlador usa esta implementación

    @MockBean
    private UserService userService;

    // --- MOCKS PARA SILENCIAR EL ERROR DE JPA ---
    // Al declarar estos @MockBean, Spring no intentará crear las instancias reales
    // que requieren conexión a base de datos, evitando el error de "QueryCreationException".
    @MockBean
    private JpaMessageRepository jpaMessageRepository;

    @MockBean
    private JpaUserRepository jpaUserRepository;


    /**
     * Configuración mínima para satisfacer @EnableJpaRepositories en MessageApplication.
     * Provee los beans de infraestructura básicos (aunque sean falsos) para que la app arranque.
     */
    @TestConfiguration
    static class BypassJpaConfig {
        @Bean
        public DataSource dataSource() {
            return Mockito.mock(DataSource.class);
        }
        @Bean
        public EntityManagerFactory entityManagerFactory() {
            return Mockito.mock(EntityManagerFactory.class);
        }
        @Bean
        public PlatformTransactionManager transactionManager() {
            return Mockito.mock(PlatformTransactionManager.class);
        }
    }

    @BeforeEach
    void setUp() {
        // Datos de prueba "hardcoded"
        User userLuchito = new User(
                UserId.from(10L),
                "Luchito",
                "lucho1234@mock.com",
                UserStatus.ONLINE,
                null
        );

        User userEloysito = new User(
                UserId.from(20L),
                "Eloysito",
                "eloy@mock.com",
                UserStatus.OFFLINE,
                LocalDateTime.now()
        );

        List<User> mockUsers = Arrays.asList(userLuchito, userEloysito);

        // Configuramos el comportamiento del repositorio mockeado
        given(userRepository.findOnlineUsers()).willReturn(mockUsers);
    }

    @Test
    void testGetOnlineUsers_ReturnsOkAndList() throws Exception {
        mockMvc.perform(get("/api/users/online")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetOnlineUsers_ContainsSpecificUserData() throws Exception {
        mockMvc.perform(get("/api/users/online"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("Luchito")))
                .andExpect(jsonPath("$[0].email", is("lucho1234@mock.com")));
    }

    @Test
    void testGetOnlineUsers_ReturnsCorrectCount() throws Exception {
        mockMvc.perform(get("/api/users/online"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}