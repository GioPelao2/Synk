package com.mesagge.domain.entities;

import java.time.LocalDateTime;

public class Message {

    private MessageId messageId;
    private UserId senderId;
    private UserId receiverId;
    private String content;
    private LocalDateTime timestamp;
    private MessageType messageIdType;
    private MessageStatus status;

    public void send() {
        //TODO: Funcion
    }

    public void markAsRead() {
        //TODO: Funcion
    }

    public void validate() {
        //TODO: Funcion
    }

    //TODO:Constructor
}
