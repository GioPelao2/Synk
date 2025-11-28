package com.message.config;

import com.message.application.usecase.message.*;
import com.message.domain.repositories.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageUseCaseConfiguration {

    @Bean
    public SendMessage sendMessage(MessageRepository messageRepository) {
        return new SendMessage(messageRepository);
    }

    @Bean
    public GetMessageById getMessageById(MessageRepository messageRepository) {
        return new GetMessageById(messageRepository);
    }

    @Bean
    public MarkMessageAsRead markMessageAsRead(MessageRepository messageRepository) {
        return new MarkMessageAsRead(messageRepository);
    }

    @Bean
    public MarkAllMessagesAsRead markAllMessagesAsRead(MessageRepository messageRepository) {
        return new MarkAllMessagesAsRead(messageRepository);
    }

    @Bean
    public ValidateMessage validateMessage(MessageRepository messageRepository) {
        return new ValidateMessage(messageRepository);
    }

    @Bean
    public CheckIfMessageIsFromUser checkIfMessageIsFromUser(MessageRepository messageRepository) {
        return new CheckIfMessageIsFromUser(messageRepository);
    }

    @Bean
    public CheckIfMessageIsToUser checkIfMessageIsToUser(MessageRepository messageRepository) {
        return new CheckIfMessageIsToUser(messageRepository);
    }

    @Bean
    public CheckIfMessageIsRead checkIfMessageIsRead(MessageRepository messageRepository) {
        return new CheckIfMessageIsRead(messageRepository);
    }

    @Bean
    public GetConversationHistory getConversationHistory(MessageRepository messageRepository) {
        return new GetConversationHistory(messageRepository);
    }

    @Bean
    public GetUnreadMessagesCount getUnreadMessagesCount(MessageRepository messageRepository) {
        return new GetUnreadMessagesCount(messageRepository);
    }
}
