package com.message.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.MessageRepositoryImpl;
import com.message.infrastructure.persistence.UserRepositoryImpl;
import com.message.presentation.dto.MessageDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageRepositoryImpl messageRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    // Enviar mensaje
    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        if (messageDTO.getContent() == null || messageDTO.getContent().trim().isEmpty() ||
            messageDTO.getSenderId() == null || messageDTO.getReceiverId() == null) {
            return ResponseEntity.badRequest().build();
        }

        UserId senderId = UserId.from(messageDTO.getSenderId());
        UserId receiverId = UserId.from(messageDTO.getReceiverId());

        Optional<User> sender = userRepository.findById(senderId);
        Optional<User> receiver = userRepository.findById(receiverId);

        if (sender.isEmpty() || receiver.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Crear y guardar mensaje
        Message newMessage = new Message(senderId, receiverId, messageDTO.getContent());
        Message savedMessage = messageRepository.save(newMessage);

        MessageDTO response = new MessageDTO(savedMessage);
        response.setSenderUsername(sender.get().getUsername());
        response.setReceiverUsername(receiver.get().getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Obtener conversación entre dos usuarios
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @PathVariable Long userId1, @PathVariable Long userId2) {

        UserId id1 = UserId.from(userId1);
        UserId id2 = UserId.from(userId2);

        List<Message> messages = messageRepository.findConversationHistory(id1, id2);

        Optional<User> user1 = userRepository.findById(id1);
        Optional<User> user2 = userRepository.findById(id2);

        if (user1.isEmpty() || user2.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Mapear a DTOs
        List<MessageDTO> messageDTOs = messages.stream()
            .map(message -> {
                MessageDTO dto = new MessageDTO(message);
                // Asignar usernames según quien envía
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
    }

    // Obtener mensajes no leídos para un usuario
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<MessageDTO>> getUnreadMessages(@PathVariable Long userId) {
        UserId id = UserId.from(userId);

        if (userRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Message> unreadMessages = messageRepository.findUnreadMessagesByUserId(id);

        List<MessageDTO> dtos = unreadMessages.stream()
            .map(MessageDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // Marcar mensaje como leído
    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        MessageId id = MessageId.from(messageId);

        if (messageRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        messageRepository.markMessageAsRead(id);
        return ResponseEntity.ok().build();
    }

    // Contar mensajes no leídos
    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        UserId id = UserId.from(userId);

        if (userRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Long count = messageRepository.countUnreadMessages(id);
        return ResponseEntity.ok(count);
    }
}
