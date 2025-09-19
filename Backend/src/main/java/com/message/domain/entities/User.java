package com.message.domain.entities;

import com.message.domain.valueobjects.UserId;
import com.message.domain.enums.UserStatus;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * TODO: Agregar campos como avatar, nombre completo, etc.
 */
public class User {
    private UserId id;
    private String username; // unico
    private String email;    // unico
    private UserStatus status;
    private LocalDateTime lastSeen;

    // Constructor para usuarios que ya existen en BD
    public User(UserId id, String username, String email, UserStatus status, LocalDateTime lastSeen) {
        this.id = Objects.requireNonNull(id, "UserId cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }

    // Constructor para usuarios nuevos (registro)
    public User(String username, String email) {
        this.id = UserId.newId(); // ID temporal hasta que se guarde en BD
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }

    /**
     * Método para asignar ID real despues de guardar en BD
     * similar al de Message, funciona bien
     */
    public User withId(UserId newId) {
        if (!this.id.isTemporary()) {
            throw new IllegalStateException("User already has an ID assigned");
        }
        return new User(newId, this.username, this.email, this.status, this.lastSeen);
    }

    // Métodos para cambiar el estado del usuario
    public void goOnline() {
        this.status = UserStatus.ONLINE;
        this.lastSeen = LocalDateTime.now();
    }

    public void goOffline() {
        this.status = UserStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }

    public void goAway() {
        this.status = UserStatus.AWAY; // Cuando está inactivo pero conectado, posiblemente automatico
        this.lastSeen = LocalDateTime.now();
    }

    // Método genérico para cambiar estado ¿removible?
    public void changeStatus(UserStatus newStatus) {
        Objects.requireNonNull(newStatus, "UserStatus cannot be null");
        this.status = newStatus;
        this.lastSeen = LocalDateTime.now();
    }

    /**
     * Lógica de negocio: cuándo un usuario puede recibir mensajes
     * TODO: Revisar si queremos permitir mensajes a usuarios OFFLINE
     */
    public boolean canReceiveMessage(UserId senderId) {
        //no puedes mandarte mensajes a ti mismo
        if (this.id.equals(senderId)) {
            return false;
        }
        // Solo usuarios activos pueden recibir mensajes inmediatamente
        return this.status == UserStatus.ONLINE || this.status == UserStatus.AWAY;
    }

    // Métodos de conveniencia para verificar estado
    public boolean isOnline() {
        return this.status == UserStatus.ONLINE;
    }

    public boolean isAway() {
        return this.status == UserStatus.AWAY;
    }

    public boolean isOffline() {
        return this.status == UserStatus.OFFLINE;
    }

    public UserId getId() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public UserStatus getStatus() {return status;}
    public LocalDateTime getLastSeen() {return lastSeen;}

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        User user = (User) object;
        return Objects.equals(id, user.id); // Usuarios son iguales si tienen el mismo ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
