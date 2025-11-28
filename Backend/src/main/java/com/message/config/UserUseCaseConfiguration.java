package com.message.config;

import com.message.application.usecase.user.*;
import com.message.domain.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfiguration {


    @Bean
    public CreateUser createUser(UserRepository userRepository) {
        return new CreateUser(userRepository);
    }

    @Bean
    public GetUserById getUserById(UserRepository userRepository) {
        return new GetUserById(userRepository);
    }

    @Bean
    public GetAllUsers getAllUsers(UserRepository userRepository) {
        return new GetAllUsers(userRepository);
    }

    @Bean
    public DeleteUser deleteUser(UserRepository userRepository) {
        return new DeleteUser(userRepository);
    }


    @Bean
    public SetUserOnline setUserOnline(UserRepository userRepository) {
        return new SetUserOnline(userRepository);
    }

    @Bean
    public SetUserOffline setUserOffline(UserRepository userRepository) {
        return new SetUserOffline(userRepository);
    }

    @Bean
    public SetUserAway setUserAway(UserRepository userRepository) {
        return new SetUserAway(userRepository);
    }

    @Bean
    public GetUserStatus getUserStatus(UserRepository userRepository) {
        return new GetUserStatus(userRepository);
    }


    @Bean
    public CheckIfUserIsOnline checkIfUserIsOnline(UserRepository userRepository) {
        return new CheckIfUserIsOnline(userRepository);
    }

    @Bean
    public CheckIfUserIsAway checkIfUserIsAway(UserRepository userRepository) {
        return new CheckIfUserIsAway(userRepository);
    }

    @Bean
    public CheckIfUserIsOffline checkIfUserIsOffline(UserRepository userRepository) {
        return new CheckIfUserIsOffline(userRepository);
    }


    @Bean
    public FindUserByUsername findUserByUsername(UserRepository userRepository) {
        return new FindUserByUsername(userRepository);
    }

    @Bean
    public FindUserByEmail findUserByEmail(UserRepository userRepository) {
        return new FindUserByEmail(userRepository);
    }

    @Bean
    public GetUserLastSeen getUserLastSeen(UserRepository userRepository) {
        return new GetUserLastSeen(userRepository);
    }


    @Bean
    public GetOnlineUsers getOnlineUsers(UserRepository userRepository) {
        return new GetOnlineUsers(userRepository);
    }


    @Bean
    public GetOfflineUsers getOfflineUsers(UserRepository userRepository) {
        return new GetOfflineUsers(userRepository);
    }


    @Bean
    public CheckUsernameAvailability checkUsernameAvailability(UserRepository userRepository) {
        return new CheckUsernameAvailability(userRepository);
    }

    @Bean
    public CheckEmailAvailability checkEmailAvailability(UserRepository userRepository) {
        return new CheckEmailAvailability(userRepository);
    }

    @Bean
    public CheckUserExists checkUserExists(UserRepository userRepository) {
        return new CheckUserExists(userRepository);
    }


    @Bean
    public CheckIfUserCanReceiveMessage checkIfUserCanReceiveMessage(UserRepository userRepository) {
        return new CheckIfUserCanReceiveMessage(userRepository);
    }
}
