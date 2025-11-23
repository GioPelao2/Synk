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
       // La lógica de validación se ha extraído a métodos privados para mayor claridad
        validateContentIsPresent(content);
        validateNotSendingToSelf(senderId, receiverId);
        
        Optional<User> senderOpt = validateSenderExists(senderId);
        Optional<User> receiverOpt = validateReceiverExists(receiverId);

        validateReceiverCanReceive(receiverOpt.get(), senderId);

        Message message = new Message(senderId, receiverId, content.trim());
        return messageRepository.save(message);
    }

    public Optional<Message> getMessageById(MessageId messageId) {
        return messageRepository.findById(messageId);
    }

    public List<Message> getConversationHistory(UserId user1Id, UserId user2Id) {
        validateUserExists(user1Id);
        validateUserExists(user2Id);

        return messageRepository.findConversationHistory(user1Id, user2Id);
    }

    public List<Message> getUnreadMessages(UserId userId) {
        validateUserExists(userId);

        return messageRepositoryImpl.findUnreadMessagesByUserId(userId);
    }

    public void markMessageAsRead(MessageId messageId, UserId userId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found with id: " + messageId);
        }

        Message message = messageOpt.get();

        if (!message.getReceiverId().equals(userId)) {
            throw new IllegalArgumentException("User is not the receiver of this message");
        }

        messageRepositoryImpl.markMessageAsRead(messageId);
    }

    public void markConversationAsRead(UserId receiverId, UserId senderId) {
        validateReceiverExists(receiverId);
        validateSenderExists(senderId);

        messageRepositoryImpl.markAllMessagesAsRead(senderId, receiverId);
    }

   
    public List<UserId> getConversationPartners(UserId userId) {
        validateUserExists(userId);

        return messageRepositoryImpl.findConversationPartners(userId);
    }

    public List<Message> getMessagesSentByUser(UserId userId) {
        validateUserExists(userId);

        return messageRepositoryImpl.findMessagesSentByUser(userId);
    }

    public List<Message> getMessagesReceivedByUser(UserId userId) {
        validateUserExists(userId);

        return messageRepositoryImpl.findMessagesReceivedByUser(userId);
    }

    public Optional<Message> getLastMessageBetweenUsers(UserId user1Id, UserId user2Id) {
        validateUserExists(user1Id);
        validateUserExists(user2Id);

        return messageRepositoryImpl.findLastMessageBetweenUsers(user1Id, user2Id);
    }

    public Long countUnreadMessages(UserId userId) {
        validateUserExists(userId);

        return messageRepositoryImpl.countUnreadMessages(userId);
    }

    public boolean isUserPartOfMessage(UserId userId, MessageId messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            return false;
        }

        Message message = messageOpt.get();
        return message.getSenderId().equals(userId) || message.getReceiverId().equals(userId);
    }

    public List<Message> searchMessagesByContent(UserId userId, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        validateUserExists(userId);

        // Implementar búsqueda de contenido en MessageRepository
        return List.of();
    }

    public void deleteMessage(MessageId messageId, UserId userId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found with id: " + messageId);
        }

        Message message = messageOpt.get();

        if (!message.getSenderId().equals(userId)) {
            throw new IllegalArgumentException("Only the sender can delete a message");
        }

        // Implementar lógica de Soft Delete
        throw new UnsupportedOperationException("Message deletion not yet implemented");
    }

    public MessageStatistics getMessageStatistics(UserId userId) {
        validateUserExists(userId);

        long sentMessages = messageRepositoryImpl.findMessagesSentByUser(userId).size();
        long receivedMessages = messageRepositoryImpl.findMessagesReceivedByUser(userId).size();
        long unreadMessages = messageRepositoryImpl.countUnreadMessages(userId);
        long conversationPartners = messageRepositoryImpl.findConversationPartners(userId).size();

        return new MessageStatistics(sentMessages, receivedMessages, unreadMessages, conversationPartners);
    }

    // *************************************************************
    // Refact - MÉTODOS DE VALIDACIÓN EXTRAÍDOS (Reemplazan comentarios en la lógica)
    // *************************************************************

    private void validateContentIsPresent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
    }

    private void validateNotSendingToSelf(UserId senderId, UserId receiverId) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send message to yourself");
        }
    }
    
    // NOTA: Se combina la verificación de existencia para Sender y Receiver
    private Optional<User> validateSenderExists(UserId senderId) {
        Optional<User> senderOpt = userRepository.findById(senderId);
        if (senderOpt.isEmpty()) {
            throw new IllegalArgumentException("Sender not found with id: " + senderId);
        }
        return senderOpt;
    }
    
    private Optional<User> validateReceiverExists(UserId receiverId) {
        Optional<User> receiverOpt = userRepository.findById(receiverId);
        if (receiverOpt.isEmpty()) {
            throw new IllegalArgumentException("Receiver not found with id: " + receiverId);
        }
        return receiverOpt;
    }

    private void validateReceiverCanReceive(User receiver, UserId senderId) {
        if (!receiver.canReceiveMessage(senderId)) {
            throw new IllegalArgumentException("Receiver cannot receive messages at this time");
        }
    }
    
    private void validateUserExists(UserId userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
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
