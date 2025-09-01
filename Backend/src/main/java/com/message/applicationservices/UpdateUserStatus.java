package com.message.applicationservices;

import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import com.message.domainservices.UserDomainService;

public class UpdateUserStatus {
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    
    public UpdateUserStatus(UserRepository userRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }
    
    public void execute(UserId userId, UserStatus newStatus) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (newStatus == null) {
            throw new IllegalArgumentException("UserStatus cannot be null");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        if (!userDomainService.isValidUserStatus(user)) {
            throw new IllegalStateException("User is in an invalid state");
        }
        
        UserStatus currentStatus = user.getStatus();
        if (currentStatus == newStatus) {
            return;
        }
        
        switch (newStatus) {
            case ONLINE:
                user.goOnline();
                break;
            case OFFLINE:
                user.goOffline();
                break;
            case AWAY:
                user.goAway();
                break;
            default:
                throw new IllegalArgumentException("Unsupported user status: " + newStatus);
        }
        userRepository.saveUser(user);
    }
}
