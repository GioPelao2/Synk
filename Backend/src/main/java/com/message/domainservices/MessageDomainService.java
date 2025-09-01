package com.message.domainservices;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;

public class MessageDomainService {
    
    public boolean canSendMessage(User sender, User receiver) {
        return receiver.canReceiveMessage(sender.getId());
    }

    public boolean validateMessage(Message message) {
        message.validate();
        return true;
    }
    
    //validar solo el contenido
    public boolean validateMessage(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }
        if (content.length() > 1000) { // Límite de caracteres
            throw new IllegalArgumentException("Message content is too long");
        }
        return true;
    }
    //Innecesario?
    public int calculateMessagePriority(Message message) {
        return 0; // Implementación básica
    }
}
