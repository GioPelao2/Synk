package com.message.domainservices;

import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;
import com.message.domain.repositories.UserRepository;

public class UserDomainService {
    private UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean canUsersInteract(UserId senderId, UserId receiverId) {
        // No se puede enviar mensajes a uno mismo
        if (senderId.equals(receiverId)) {
            return false;
        }
        
        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);
        
        if (sender == null || receiver == null) {
            return false;
        }
        
        return receiver.canReceiveMessage(senderId);
    }

    public boolean isValidUserStatus(User user) {
        return user != null && user.getStatus() != null;
    }
}
