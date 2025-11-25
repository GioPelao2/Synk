package com.message.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.message.application.usecase.message.*;
import com.message.application.usecase.user.GetUserById;
import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.presentation.dto.MessageDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    // ==================== USE CASES ====================
    private final SendMessage sendMessage;
    private final GetMessageById getMessageById;
    private final MarkMessageAsRead markMessageAsRead;
    private final MarkAllMessagesAsRead markAllMessagesAsRead;
    private final GetConversationHistory getConversationHistory;
    private final GetUnreadMessagesCount getUnreadMessagesCount;
    
    private final GetUserById getUserById;

    @Autowired
    public MessageController(
            SendMessage sendMessage,
            GetMessageById getMessageById,
            MarkMessageAsRead markMessageAsRead,
            MarkAllMessagesAsRead markAllMessagesAsRead,
            GetConversationHistory getConversationHistory,
            GetUnreadMessagesCount getUnreadMessagesCount,
            GetUserById getUserById) {
        this.sendMessage = sendMessage;
        this.getMessageById = getMessageById;
        this.markMessageAsRead = markMessageAsRead;
        this.markAllMessagesAsRead = markAllMessagesAsRead;
        this.getConversationHistory = getConversationHistory;
        this.getUnreadMessagesCount = getUnreadMessagesCount;
        this.getUserById = getUserById;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {
        try {
            // Validación básica
            if (messageDTO.getContent() == null || messageDTO.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content cannot be empty");
            }
            if (messageDTO.getSenderId() == null || messageDTO.getReceiverId() == null) {
                return ResponseEntity.badRequest().body("Sender and receiver IDs are required");
            }

            UserId senderId = UserId.from(messageDTO.getSenderId());
            UserId receiverId = UserId.from(messageDTO.getReceiverId());

            Optional<User> sender = getUserById.execute(senderId);
            Optional<User> receiver = getUserById.execute(receiverId);

            if (sender.isEmpty() || receiver.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sender or receiver not found");
            }

            Message savedMessage = sendMessage.execute(senderId, receiverId, messageDTO.getContent());

            MessageDTO response = new MessageDTO(savedMessage);
            response.setSenderUsername(sender.get().getUsername());
            response.setReceiverUsername(receiver.get().getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Long id) {
        try {
            Optional<Message> message = getMessageById.execute(MessageId.from(id));

            if (message.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            MessageDTO dto = new MessageDTO(message.get());
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<?> getConversation(
            @PathVariable Long userId1,
            @PathVariable Long userId2) {
        try {
            UserId id1 = UserId.from(userId1);
            UserId id2 = UserId.from(userId2);

            Optional<User> user1 = getUserById.execute(id1);
            Optional<User> user2 = getUserById.execute(id2);

            if (user1.isEmpty() || user2.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("One or both users not found");
            }

            List<Message> messages = getConversationHistory.execute(id1, id2);

            List<MessageDTO> messageDTOs = messages.stream()
                .map(message -> {
                    MessageDTO dto = new MessageDTO(message);
                    if (message.getSenderId().equals(id1)) {
                        dto.setSenderUsername(user1.get().getUsername());
                        dto.setReceiverUsername(user2.get().getUsername());
                    } else {
                        dto.setSenderUsername(user2.get().getUsername());
                        dto.setReceiverUsername(user1.get().getUsername());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOs);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<?> getUnreadCount(@PathVariable Long userId) {
        try {
            UserId id = UserId.from(userId);

            if (getUserById.execute(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
            }

            long count = getUnreadMessagesCount.execute(id);
            return ResponseEntity.ok(count);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long messageId,
            @RequestParam Long userId) {
        try {
            MessageId msgId = MessageId.from(messageId);
            UserId usrId = UserId.from(userId);

            markMessageAsRead.execute(msgId, usrId);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/conversation/{userId1}/{userId2}/read-all")
    public ResponseEntity<?> markAllAsRead(
            @PathVariable Long userId1,
            @PathVariable Long userId2) {
        try {
            UserId receiverId = UserId.from(userId1);
            UserId senderId = UserId.from(userId2);

            markAllMessagesAsRead.execute(receiverId, senderId);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }   
}
