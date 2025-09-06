package com.message.infrastructure.persistance;

import java.util.List;
import java.util.Optional;
import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMessageRepository extends JpaRepository<Message, MessageId>, MessageRepository {
    
    Optional<Message> findById(MessageId id);
    
    @Override
    default Message save(Message message) {
        return save(message);
    }
    
    @Override
    @Query("SELECT m FROM Message m WHERE " +
           "(m.senderId = :id1 AND m.receiverId = :id2) OR " +
           "(m.senderId = :id2 AND m.receiverId = :id1) " +
           "ORDER BY m.timestamp ASC")
    List<Message> findConversationHistory(@Param("id1") UserId id1, @Param("id2") UserId id2);
    
    // Métodos adicionales útiles para un sistema de mensajería
    @Query("SELECT m FROM Message m WHERE m.receiverId = :userId AND m.isRead = false ORDER BY m.timestamp DESC")
    List<Message> findUnreadMessagesByUserId(@Param("userId") UserId userId);
    
    @Query("SELECT DISTINCT CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END " +
           "FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId")
    List<UserId> findConversationPartners(@Param("userId") UserId userId);
    
    @Query("SELECT m FROM Message m WHERE m.senderId = :userId ORDER BY m.timestamp DESC")
    List<Message> findMessagesSentByUser(@Param("userId") UserId userId);
}
