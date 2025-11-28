package com.message.config;

import com.message.application.usecase.conversation.*;
import com.message.domain.repositories.ConversationRepository;
import com.message.domain.repositories.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversationUseCaseConfiguration {

    @Bean
    public CreateConversation createConversation(ConversationRepository conversationRepository) {
        return new CreateConversation(conversationRepository);
    }

    @Bean
    public GetConversationById getConversationById(ConversationRepository conversationRepository) {
        return new GetConversationById(conversationRepository);
    }

    @Bean
    public GetAllConversations getAllConversations(ConversationRepository conversationRepository) {
        return new GetAllConversations(conversationRepository);
    }

    @Bean
    public GetUserConversations getUserConversations(ConversationRepository conversationRepository) {
        return new GetUserConversations(conversationRepository);
    }

    @Bean
    public GetConversationByParticipants getConversationByParticipants(ConversationRepository conversationRepository) {
        return new GetConversationByParticipants(conversationRepository);
    }

    @Bean
    public GetOrCreateConversation getOrCreateConversation(ConversationRepository conversationRepository) {
        return new GetOrCreateConversation(conversationRepository);
    }

    @Bean
    public DeleteConversation deleteConversation(ConversationRepository conversationRepository) {
        return new DeleteConversation(conversationRepository);
    }

    @Bean
    public CheckConversationExists checkConversationExists(ConversationRepository conversationRepository) {
        return new CheckConversationExists(conversationRepository);
    }

    @Bean
    public CheckConversationExistsBetweenUsers checkConversationExistsBetweenUsers(ConversationRepository conversationRepository) {
        return new CheckConversationExistsBetweenUsers(conversationRepository);
    }

    @Bean
    public CheckIfUserIsParticipant checkIfUserIsParticipant(ConversationRepository conversationRepository) {
        return new CheckIfUserIsParticipant(conversationRepository);
    }

    @Bean
    public GetUnreadCountForConversation getUnreadCountForConversation(ConversationRepository conversationRepository) {
        return new GetUnreadCountForConversation(conversationRepository);
    }

    @Bean
    public MarkConversationAsReadFor markConversationAsReadFor(ConversationRepository conversationRepository) {
        return new MarkConversationAsReadFor(conversationRepository);
    }

    @Bean
    public AddMessageToConversation addMessageToConversation(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository) {
        return new AddMessageToConversation(conversationRepository, messageRepository);
    }
}
