package com.message.presentation.controller;

import com.message.application.usecase.user.*;
import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.valueobjects.UserId;
import com.message.presentation.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
// Esta línea es CRÍTICA: Le dice a Spring que use nuestra 'TestApplication' interna
// en lugar de buscar la 'MessageApplication' real que tiene la configuración de DB rota.
@ContextConfiguration(classes = UserControllerMockUsersTest.TestApplication.class)
class UserControllerMockUsersTest {

    /**
     * Mini-Aplicación para el Test.
     * Al definir esto, evitamos cargar 'MessageApplication'.
     * Como esta clase NO tiene @EnableJpaRepositories, Spring no intentará validar
     * los repositorios JPA y el error desaparecerá.
     */
    @SpringBootApplication(exclude = {
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class
    })
    // Solo escaneamos el UserController para mantener el contexto ligero
    @ComponentScan(
            basePackageClasses = {UserController.class},
            useDefaultFilters = false,
            includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserController.class)
    )
    static class TestApplication {
    }

    @Autowired
    private MockMvc mockMvc;

    // --- MOCKS DE CASOS DE USO ---
    // Declaramos todos los componentes que el UserController necesita en su constructor.

    @MockBean private GetOnlineUsers getOnlineUsers;
    @MockBean private GetUserById getUserById;
    @MockBean private CreateUser createUser;
    @MockBean private FindUserByUsername findUserByUsername;
    @MockBean private GetAllUsers getAllUsers;
    @MockBean private SetUserOnline setUserOnline;
    @MockBean private SetUserOffline setUserOffline;
    @MockBean private SetUserAway setUserAway;
    @MockBean private CheckUsernameAvailability checkUsernameAvailability;
    @MockBean private CheckEmailAvailability checkEmailAvailability;
    @MockBean private DeleteUser deleteUser;

    // Mock del mapper (necesario para que Spring pueda instanciar el controlador)
    @MockBean private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // DATOS DE PRUEBA ("ESPEJO")
        // Coinciden con los datos hardcoded originales para mantener la consistencia de las pruebas
        User userLuchito = new User(
                UserId.from(1L),
                "Luchito",
                "lucho1234@mock.com",
                UserStatus.ONLINE,
                null
        );

        User userEloysito = new User(
                UserId.from(2L),
                "Eloysito",
                "eloy@mock.com",
                UserStatus.OFFLINE,
                LocalDateTime.now()
        );

        // Configuramos el mock del caso de uso GetOnlineUsers
        given(getOnlineUsers.execute()).willReturn(Arrays.asList(userLuchito, userEloysito));
    }

    /**
     * PRUEBA 1: Obtener Usuarios Online
     * Verifica que el endpoint responda con una lista JSON.
     */
    @Test
    void testGetOnlineUsers_ReturnsOkAndList() throws Exception {
        mockMvc.perform(get("/api/users/online")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /**
     * PRUEBA 2: Integridad de Datos
     * Verifica que la respuesta contenga el usuario "Luchito" (el dato de control).
     */
    @Test
    void testGetOnlineUsers_ContainsSpecificUserData() throws Exception {
        mockMvc.perform(get("/api/users/online")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("Luchito")))
                .andExpect(jsonPath("$[0].email", is("lucho1234@mock.com")));
    }

    /**
     * PRUEBA 3: Registro de Usuario
     * Valida el flujo de creación exitosa (201 Created).
     */
    @Test
    void testRegisterUser_ReturnsCreated() throws Exception {
        // Arrange
        User savedUser = new User(UserId.from(5L), "Nuevo", "nuevo@test.com", UserStatus.OFFLINE, null);

        // Simulamos la respuesta del caso de uso CreateUser
        given(createUser.execute(any(), any(), any())).willReturn(savedUser);

        String jsonBody = "{\"username\": \"Nuevo\", \"email\": \"nuevo@test.com\", \"password\": \"12345678\"}";

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.username", is("Nuevo")));
    }

    /**
     * PRUEBA 4: Obtener Usuario por ID
     * Valida que se pueda recuperar un usuario específico.
     */
    @Test
    void testGetUserById_ReturnsUser() throws Exception {
        // Arrange
        Long id = 1L;
        User user = new User(UserId.from(id), "TestUser", "test@mail.com", UserStatus.ONLINE, null);
        given(getUserById.execute(UserId.from(id))).willReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(get("/api/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("TestUser")));
    }
}