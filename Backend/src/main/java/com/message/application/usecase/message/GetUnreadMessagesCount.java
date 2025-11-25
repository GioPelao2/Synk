package com.message.application.usecase.message;

import java.util.List;

import com.message.domain.entities.Message;
import com.message.domain.repositories.MessageRepository;
import com.message.domain.valueobjects.UserId;

public class GetUnreadMessagesCount {
    private final MessageRepository messageRepository;

    public GetUnreadMessagesCount(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public long execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        // TODO: Implementar método más eficiente en repository
        // Por ahora, esto requerira traer todos los mensajes del usuario
        // y filtrarlos, lo cual es ineficiente
        
        throw new UnsupportedOperationException(
            "This use case requires MessageRepository.findUnreadMessagesByReceiver() method"
        );
    }
}
