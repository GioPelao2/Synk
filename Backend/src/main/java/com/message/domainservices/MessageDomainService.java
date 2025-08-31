package com.message.domainservices;

import com.message.domain.entities.Message;
import com.message.domain.entities.User;

public class MessageDomainService {
    
    public boolean canSendMessage(User sender, User receiver) {
        //Quizas redundante creo que la funcion compara a ambos usuarios
        if (sender.canReceiveMessage(receiver.getId()) && receiver.canReceiveMessage(sender.getId())) {
            return true;
        }
        return false;
    }

    public boolean validateMessage(Message message) {
        message.validate();
        return true;
    }
    //Innecesario??
    public int calculateMessagePriority(Message message) {
        return 0;
    }
}
