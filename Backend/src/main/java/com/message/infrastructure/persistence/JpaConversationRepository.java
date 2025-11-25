package com.message.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaConversationRepository extends JpaRepository<ConversationEntity, Long> {

    @Query("SELECT c FROM ConversationEntity c JOIN c.participantIds p1 JOIN c.participantIds p2 " +
           "WHERE p1 = :userId1 AND p2 = :userId2 AND SIZE(c.participantIds) = 2")
    Optional<ConversationEntity> findByTwoParticipants(@Param("userId1") Long userId1, 
                                                        @Param("userId2") Long userId2);

    @Query("SELECT c FROM ConversationEntity c JOIN c.participantIds p WHERE p = :userId ORDER BY c.lastMessageAt DESC")
    List<ConversationEntity> findByParticipantId(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) > 0 FROM ConversationEntity c JOIN c.participantIds p1 JOIN c.participantIds p2 " +
           "WHERE p1 = :userId1 AND p2 = :userId2 AND SIZE(c.participantIds) = 2")
    boolean existsBetweenTwoUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
