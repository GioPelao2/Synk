package com.message.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMessageRepository extends JpaRepository<MessageEntity, Long> {

    // Buscar conversación entre dos usuarios
    @Query("SELECT m FROM MessageEntity m WHERE " +
           "(m.senderId = :id1 AND m.receiverId = :id2) OR " +
           "(m.senderId = :id2 AND m.receiverId = :id1) " +
           "ORDER BY m.timestamp ASC")
    List<MessageEntity> findConversationHistory(@Param("id1") Long id1, @Param("id2") Long id2);

    // Mensajes no leídos para un usuario
    @Query("SELECT m FROM MessageEntity m WHERE m.receiverId = :userId AND m.isRead = false ORDER BY m.timestamp DESC")
    List<MessageEntity> findUnreadMessagesByUserId(@Param("userId") Long userId);

    // Encontrar todos los contactos con los que un usuario ha conversado
    @Query("SELECT DISTINCT CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END " +
           "FROM MessageEntity m WHERE m.senderId = :userId OR m.receiverId = :userId")
    List<Long> findConversationPartners(@Param("userId") Long userId);

    // Mensajes enviados por un usuario
    @Query("SELECT m FROM MessageEntity m WHERE m.senderId = :userId ORDER BY m.timestamp DESC")
    List<MessageEntity> findMessagesSentByUser(@Param("userId") Long userId);

    // Mensajes recibidos por un usuario
    @Query("SELECT m FROM MessageEntity m WHERE m.receiverId = :userId ORDER BY m.timestamp DESC")
    List<MessageEntity> findMessagesReceivedByUser(@Param("userId") Long userId);

    // Último mensaje entre dos usuarios
    @Query("SELECT m FROM MessageEntity m WHERE " +
           "(m.senderId = :id1 AND m.receiverId = :id2) OR " +
           "(m.senderId = :id2 AND m.receiverId = :id1) " +
           "ORDER BY m.timestamp DESC LIMIT 1")
    Optional<MessageEntity> findLastMessageBetweenUsers(@Param("id1") Long id1, @Param("id2") Long id2);

    // Contar mensajes no leídos
    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.receiverId = :userId AND m.isRead = false")
    Long countUnreadMessages(@Param("userId") Long userId);
}
