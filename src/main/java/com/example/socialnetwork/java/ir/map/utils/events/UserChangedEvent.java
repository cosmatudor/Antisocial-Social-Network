package com.example.socialnetwork.java.ir.map.utils.events;

import com.example.socialnetwork.java.ir.map.domain.User;

public class UserChangedEvent implements Event{

    private ChangeEventType changeEventType;
    private User user, oldUser;

    public UserChangedEvent(ChangeEventType changeEventType, User user) {
        this.changeEventType = changeEventType;
        this.user = user;
    }

    public UserChangedEvent(ChangeEventType changeEventType, User user, User oldUser) {
        this.changeEventType = changeEventType;
        this.user = user;
        this.oldUser = oldUser;
    }

    public ChangeEventType getChangeEventType() {
        return changeEventType;
    }

    public User getUser() {
        return user;
    }

    public User getOldUser() {
        return oldUser;
    }
}
