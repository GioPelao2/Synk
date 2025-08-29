package com.mesagge.domain.entities;

import java.time.LocalDateTime;

public class User {
    private UserId id;
    private String username;
    private String email;
    private UserStatus status;
    private LocalDateTime lastSeen;


    public void goOnline() {
        //TODO
    }

    public void goOfline() {
        //TODO:
    }

    public boolean canReceiveMessage(UserId senderId) {
        //TODO
    }

    public UserStatus getUserStatus(){
        return status;

    }
}
