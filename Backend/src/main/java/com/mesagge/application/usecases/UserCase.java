package com.mesagge.application.usecases;

import java.util.List;

import com.mesagge.domain.entities.User;
import com.mesagge.domain.valueobjects.UserId;

public interface UserCase {
    
    public User getUserById(UserId userid);

    public List<User> getUsersOnline();

    public void updateUserStatus(UserId userId, UserStatus status|);
    
}
