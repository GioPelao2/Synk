package com.message.applicationservices;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.MessageId;
import com.message.domain.valueobjects.UserId;
import com.message.domainservices.MessageDomainService;

public class SendMessage {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private MessageDomainService messageDomainService;

    public SendMessage(MessageRepository messageRepository, 
                      UserRepository userRepository, 
                      MessageDomainService messageDomainService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageDomainService = messageDomainService;
    }

    public void execute(UserId senderId, UserId receiverId, String content) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (messageDomainService.canSendMessage(sender, receiver)) {
            MessageId messageId = MessageId.from(System.currentTimeMillis()); // ID temporal
            Message message = new Message(messageId, senderId, receiverId, content);
            
            messageDomainService.validateMessage(message);
            
            messageRepository.save(message);
        } else {
            throw new IllegalStateException("Cannot send message between these users");
        }
    }
}
