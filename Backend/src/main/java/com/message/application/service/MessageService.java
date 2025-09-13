package com.message.application.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.infrastructure.persistence.MessageRepositoryImpl;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageRepositoryImpl messageRepositoryImpl;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                         UserRepository userRepository,
                         MessageRepositoryImpl messageRepositoryImpl) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageRepositoryImpl = messageRepositoryImpl;
    }

    public Message sendMessage(UserId senderId, UserId receiverId, String content) {
        // Validar que el contenido no esté vacío
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }

        // Verificar que el sender existe
        Optional<User> senderOpt = userRepository.findById(senderId);
        if (senderOpt.isEmpty()) {
            throw new IllegalArgumentException("Sender not found with id: " + senderId);
        }

        // Verificar que el receiver existe
        Optional<User> receiverOpt = userRepository.findById(receiverId);
        if (receiverOpt.isEmpty()) {
            throw new IllegalArgumentException("Receiver not found with id: " + receiverId);
        }

        // Verificar que no sea el mismo usuario
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send message to yourself");
        }

        // Verificar que el receptor puede recibir mensajes
        User receiver = receiverOpt.get();
        if (!receiver.canReceiveMessage(senderId)) {
            throw new IllegalArgumentException("Receiver cannot receive messages at this time");
        }

        Message message = new Message(senderId, receiverId, content.trim());
        return messageRepository.save(message);
    }

    public Optional<Message> getMessageById(MessageId messageId) {
        return messageRepository.findById(messageId);
    }

    // Obtener historial de conversación entre dos usuarios
    public List<Message> getConversationHistory(UserId user1Id, UserId user2Id) {
        // Verificar que ambos usuarios existan
        if (!userRepository.findById(user1Id).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + user1Id);
        }
        if (!userRepository.findById(user2Id).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + user2Id);
        }

        return messageRepository.findConversationHistory(user1Id, user2Id);
    }

    // Obtener mensajes no leídos para un usuario
    public List<Message> getUnreadMessages(UserId userId) {
        // Verificar que el usuario existe
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        return messageRepositoryImpl.findUnreadMessagesByUserId(userId);
    }

    public void markMessageAsRead(MessageId messageId, UserId userId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found with id: " + messageId);
        }

        Message message = messageOpt.get();

        // Verificar que el usuario es el receptor del mensaje
        if (!message.getReceiverId().equals(userId)) {
            throw new IllegalArgumentException("User is not the receiver of this message");
        }

        messageRepositoryImpl.markMessageAsRead(messageId);
    }

    // Marcar todos los mensajes de una conversación como leídos
    public void markConversationAsRead(UserId receiverId, UserId senderId) {
        // Verificar que ambos usuarios existan
        if (!userRepository.findById(receiverId).isPresent()) {
            throw new IllegalArgumentException("Receiver not found with id: " + receiverId);
        }
        if (!userRepository.findById(senderId).isPresent()) {
            throw new IllegalArgumentException("Sender not found with id: " + senderId);
        }

        messageRepositoryImpl.markAllMessagesAsRead(senderId, receiverId);
    }

    // Obtener contactos de conversación de un usuario
    public List<UserId> getConversationPartners(UserId userId) {
        // Verificar que el usuario existe
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        return messageRepositoryImpl.findConversationPartners(userId);
    }

    // Obtener mensajes enviados por un usuario
    public List<Message> getMessagesSentByUser(UserId userId) {
        // Verificar que el usuario existe
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        return messageRepositoryImpl.findMessagesSentByUser(userId);
    }

    // Obtener mensajes recibidos por un usuario
    public List<Message> getMessagesReceivedByUser(UserId userId) {
        // Verificar que el usuario existe
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        return messageRepositoryImpl.findMessagesReceivedByUser(userId);
    }

    // Obtener el último mensaje entre dos usuarios
    public Optional<Message> getLastMessageBetweenUsers(UserId user1Id, UserId user2Id) {
        // Verificar que ambos usuarios existan
        if (!userRepository.findById(user1Id).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + user1Id);
        }
        if (!userRepository.findById(user2Id).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + user2Id);
        }

        return messageRepositoryImpl.findLastMessageBetweenUsers(user1Id, user2Id);
    }

    // Contar mensajes no leídos para un usuario
    public Long countUnreadMessages(UserId userId) {
        // Verificar que el usuario existe
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        return messageRepositoryImpl.countUnreadMessages(userId);
    }

    // Verificar si un mensaje pertenece a un usuario (como sender o receiver)
    public boolean isUserPartOfMessage(UserId userId, MessageId messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            return false;
        }

        Message message = messageOpt.get();
        return message.getSenderId().equals(userId) || message.getReceiverId().equals(userId);
    }

    // Buscar mensajes por contenido (podría implementarse en el repository)
    public List<Message> searchMessagesByContent(UserId userId, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        // Verificar que el usuario existe
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        // Esta funcionalidad requeriría un método adicional en el repository
        // Por ahora, devuelvo una lista vacía como placeholder
        return List.of();
    }

    // Eliminar un mensaje (soft delete podría implementarse)
    public void deleteMessage(MessageId messageId, UserId userId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found with id: " + messageId);
        }

        Message message = messageOpt.get();

        // Verificar que el usuario es el sender del mensaje
        if (!message.getSenderId().equals(userId)) {
            throw new IllegalArgumentException("Only the sender can delete a message");
        }

        // Aquí implementarías la lógica de eliminación
        // Podría ser un soft delete añadiendo un campo 'deleted' al mensaje
        throw new UnsupportedOperationException("Message deletion not yet implemented");
    }

    // Obtener estadísticas de mensajes para un usuario
    public MessageStatistics getMessageStatistics(UserId userId) {
        // Verificar que el usuario existe
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        long sentMessages = messageRepositoryImpl.findMessagesSentByUser(userId).size();
        long receivedMessages = messageRepositoryImpl.findMessagesReceivedByUser(userId).size();
        long unreadMessages = messageRepositoryImpl.countUnreadMessages(userId);
        long conversationPartners = messageRepositoryImpl.findConversationPartners(userId).size();

        return new MessageStatistics(sentMessages, receivedMessages, unreadMessages, conversationPartners);
    }

    // Clase interna para estadísticas
    public static class MessageStatistics {
        private final long sentMessages;
        private final long receivedMessages;
        private final long unreadMessages;
        private final long conversationPartners;

        public MessageStatistics(long sentMessages, long receivedMessages,
                               long unreadMessages, long conversationPartners) {
            this.sentMessages = sentMessages;
            this.receivedMessages = receivedMessages;
            this.unreadMessages = unreadMessages;
            this.conversationPartners = conversationPartners;
        }

        // Getters
        public long getSentMessages() { return sentMessages; }
        public long getReceivedMessages() { return receivedMessages; }
        public long getUnreadMessages() { return unreadMessages; }
        public long getConversationPartners() { return conversationPartners; }
        public long getTotalMessages() { return sentMessages + receivedMessages; }

        @Override
        public String toString() {
            return "MessageStatistics{" +
                    "sentMessages=" + sentMessages +
                    ", receivedMessages=" + receivedMessages +
                    ", unreadMessages=" + unreadMessages +
                    ", conversationPartners=" + conversationPartners +
                    ", totalMessages=" + getTotalMessages() +
                    '}';
        }
    }
}
