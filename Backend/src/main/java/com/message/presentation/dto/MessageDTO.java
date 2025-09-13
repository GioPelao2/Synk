package com.message.presentation.dto;

import java.time.LocalDateTime;

import com.message.domain.entities.Message;

public class MessageDTO {
    private String content;
    private LocalDateTime timestamp;

    public MessageDTO(Message message) {
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
    }

    public void execute() {

    }
}
