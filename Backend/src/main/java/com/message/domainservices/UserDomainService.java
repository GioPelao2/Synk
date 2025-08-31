package com.message.domainservices;

import com.message.domain.entities.User;
import com.message.domain.valueobjects.UserId;

public class UserDomainService {
    public boolean canUsersInteract(UserId senderId, UserId receiverId) {
        
    }

    public boolean isValidUserStatus(User user) {
        return true;
    }
}
