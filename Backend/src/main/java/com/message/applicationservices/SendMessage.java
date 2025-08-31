package com.message.applicationservices;

import com.message.domain.repositories.MessageRepository;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;
import com.message.domainservices.MessageDomainService;

public class SendMessage {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private MessageDomainService messageDomainService;

    public void execute(UserId userId, UserId userId2, String message) {
        if (messageDomainService.canSendMessage(userId, userId2)==true){
            messageDomainService.validateMessage(message);
            
 {
            
        }
            }


}
