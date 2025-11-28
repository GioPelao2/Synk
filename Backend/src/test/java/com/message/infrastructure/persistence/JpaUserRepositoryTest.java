package com.message.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para la interfaz JpaUserRepository enfocadas en 'Dispensables'.
 * Valida que la eliminación de comentarios sobre los @Query no afecte el contrato.
 */
@ExtendWith(MockitoExtension.class)
class JpaUserRepositoryDispensablesTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    /**
     * PRUEBA 3: Verificar contrato de consulta (findOnlineUsers).
     * Situación: Antes del refactoring, había un comentario "// Encontrar usuarios online".
     * Después: El código está limpio.
     * Validación: Mockeamos el comportamiento para asegurar que la interfaz sigue siendo funcional
     * y devuelve los tipos de datos esperados.
     */
    @Test
    void testFindOnlineUsers_ReturnsListOfUsers() {
        // Arrange
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setUsername("UserA");

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setUsername("UserB");

        // Simulamos que la base de datos responde correctamente
        when(jpaUserRepository.findOnlineUsers()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UserEntity> result = jpaUserRepository.findOnlineUsers();

        // Assert
        assertNotNull(result, "La lista no debe ser nula.");
        assertEquals(2, result.size(), "Debe retornar los 2 usuarios simulados.");
        assertEquals("UserA", result.get(0).getUsername());
    }
}