package com.message.presentation.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.message.application.usecase.conversation.*;
import com.message.domain.entities.Conversation;
import com.message.domain.entities.Message;
import com.message.domain.valueobjects.ConversationId;
import com.message.domain.valueobjects.UserId;
import com.message.presentation.dto.ConversationDTO;
import com.message.presentation.dto.MessageDTO;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final CreateConversation createConversation;
    private final GetOrCreateConversation getOrCreateConversation;
    private final GetConversationById getConversationById;
    private final GetConversationByParticipants getConversationByParticipants;
    private final GetUserConversations getUserConversations;
    private final GetAllConversations getAllConversations;
    private final AddMessageToConversation addMessageToConversation;
    private final MarkConversationAsReadFor markConversationAsReadFor;
    private final GetUnreadCountForConversation getUnreadCountForConversation;
    private final CheckIfUserIsParticipant checkIfUserIsParticipant;
    private final DeleteConversation deleteConversation;

    @Autowired
    public ConversationController(
            CreateConversation createConversation,
            GetOrCreateConversation getOrCreateConversation,
            GetConversationById getConversationById,
            GetConversationByParticipants getConversationByParticipants,
            GetUserConversations getUserConversations,
            GetAllConversations getAllConversations,
            AddMessageToConversation addMessageToConversation,
            MarkConversationAsReadFor markConversationAsReadFor,
            GetUnreadCountForConversation getUnreadCountForConversation,
            CheckIfUserIsParticipant checkIfUserIsParticipant,
            DeleteConversation deleteConversation) {
        this.createConversation = createConversation;
        this.getOrCreateConversation = getOrCreateConversation;
        this.getConversationById = getConversationById;
        this.getConversationByParticipants = getConversationByParticipants;
        this.getUserConversations = getUserConversations;
        this.getAllConversations = getAllConversations;
        this.addMessageToConversation = addMessageToConversation;
        this.markConversationAsReadFor = markConversationAsReadFor;
        this.getUnreadCountForConversation = getUnreadCountForConversation;
        this.checkIfUserIsParticipant = checkIfUserIsParticipant;
        this.deleteConversation = deleteConversation;
    }

    @PostMapping
    public ResponseEntity<?> createConversation(@RequestParam Long participant1Id,
                                               @RequestParam Long participant2Id) {
        try {
            Conversation conversation = createConversation.execute(
                UserId.from(participant1Id),
                UserId.from(participant2Id)
            );

            ConversationDTO dto = mapToDTO(conversation);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-or-create")
    public ResponseEntity<?> getOrCreateConversation(@RequestParam Long participant1Id,
                                                     @RequestParam Long participant2Id) {
        try {
            Conversation conversation = getOrCreateConversation.execute(
                UserId.from(participant1Id),
                UserId.from(participant2Id)
            );

            ConversationDTO dto = mapToDTO(conversation);
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getConversationById(@PathVariable Long id) {
        Optional<Conversation> conversation = getConversationById.execute(ConversationId.from(id));

        if (conversation.isPresent()) {
            ConversationDTO dto = mapToDTO(conversation.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/between")
    public ResponseEntity<ConversationDTO> getConversationBetweenUsers(
            @RequestParam Long userId1,
            @RequestParam Long userId2) {
        
        Optional<Conversation> conversation = getConversationByParticipants.execute(
            UserId.from(userId1),
            UserId.from(userId2)
        );

        if (conversation.isPresent()) {
            ConversationDTO dto = mapToDTO(conversation.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(@PathVariable Long userId) {
        List<Conversation> conversations = getUserConversations.execute(UserId.from(userId));

        List<ConversationDTO> dtos = conversations.stream()
            .map(this::mapToSummaryDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getAllConversations() {
        List<Conversation> conversations = getAllConversations.execute();

        List<ConversationDTO> dtos = conversations.stream()
            .map(this::mapToSummaryDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<?> addMessage(@PathVariable Long id,
                                       @RequestParam Long senderId,
                                       @RequestParam Long receiverId,
                                       @RequestBody String content) {
        try {
            Conversation conversation = addMessageToConversation.execute(
                ConversationId.from(id),
                UserId.from(senderId),
                UserId.from(receiverId),
                content
            );

            ConversationDTO dto = mapToDTO(conversation);
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id,
                                       @RequestParam Long userId) {
        try {
            markConversationAsReadFor.execute(
                ConversationId.from(id),
                UserId.from(userId)
            );
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/unread-count")
    public ResponseEntity<?> getUnreadCount(@PathVariable Long id,
                                           @RequestParam Long userId) {
        try {
            int count = getUnreadCountForConversation.execute(
                ConversationId.from(id),
                UserId.from(userId)
            );
            return ResponseEntity.ok(count);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConversation(@PathVariable Long id) {
        try {
            deleteConversation.execute(ConversationId.from(id));
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ConversationDTO mapToDTO(Conversation conversation) {
        List<Long> participantIds = conversation.getParticipants().stream()
            .map(UserId::value)
            .collect(Collectors.toList());

        List<MessageDTO> messageDTOs = conversation.getMessages().stream()
            .map(this::mapMessageToDTO)
            .collect(Collectors.toList());

        return ConversationDTO.fullResponse(
            conversation.getId().value(),
            participantIds,
            messageDTOs,
            conversation.getCreatedAt(),
            conversation.getLastMessageAt()
        );
    }

    private ConversationDTO mapToSummaryDTO(Conversation conversation) {
        List<Long> participantIds = conversation.getParticipants().stream()
            .map(UserId::value)
            .collect(Collectors.toList());

        return ConversationDTO.summaryResponse(
            conversation.getId().value(),
            participantIds,
            conversation.getCreatedAt(),
            conversation.getLastMessageAt(),
            conversation.getMessageCount(),
            null
        );
    }

    private MessageDTO mapMessageToDTO(Message message) {
        return MessageDTO.forResponse(
            message.getMessageId().value(),
            message.getSenderId().value(),
            message.getReceiverId().value(),
            message.getContent(),
            message.getTimestamp(),
            message.isRead()
        );
    }
}
