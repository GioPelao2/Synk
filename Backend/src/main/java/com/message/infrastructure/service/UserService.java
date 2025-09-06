package com.message.infrastructure.service;

import java.util.Optional;

import com.message.applicationservices.GetUserById;
import com.message.applicationservices.UpdateUserStatus;
import com.message.domain.entities.User;
import com.message.domain.enums.UserStatus;
import com.message.domain.repositories.UserRepository;
import com.message.domain.valueobjects.UserId;

public class UserService {
    private UserRepository userRepository;
    private GetUserById getUserById;
    private UpdateUserStatus updateUserStatus;

    public UserService(UserRepository userRepository, GetUserById getUserById, UpdateUserStatus updateUserStatus) {
        this.userRepository = userRepository;
        this.getUserById = getUserById;
        this.updateUserStatus = updateUserStatus;
    }


    public Optional<User> getUser(UserId id) {
        return getUserById.execute(id);
    }

    public void updateStatus(UserId id, UserStatus status){
        updateUserStatus.execute(id, status);
    }

    public void getOnlineUsers(){
        userRepository.findOnlineUsers();
    }
}
