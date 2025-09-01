package com.message.applicationservices;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.repositories.UserRepository;
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
        // 1. Obtener los usuarios del repositorio
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // 2. Verificar si se puede enviar el mensaje
        if (messageDomainService.canSendMessage(sender, receiver)) {
            // 3. Validar el contenido antes de crear el mensaje
            messageDomainService.validateMessage(content);
            
            // 4. Crear el mensaje sin ID (el repository lo generará)
            Message message = new Message(senderId, receiverId, content);
            
            // 5. Guardar el mensaje (el repository asigna el ID automáticamente)
            messageRepository.save(message);
        } else {
            throw new IllegalStateException("Cannot send message between these users");
        }
    }
}
