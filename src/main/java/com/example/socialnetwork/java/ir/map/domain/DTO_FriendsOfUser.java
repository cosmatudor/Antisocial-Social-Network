package com.example.socialnetwork.java.ir.map.domain;

import java.time.LocalDateTime;

public class DTO_FriendsOfUser {
    String firstName;
    String secondName;
    LocalDateTime date;

    public DTO_FriendsOfUser(String firstName, String secondName, LocalDateTime date) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.date = date;
    }

    @Override
    public String toString() {
        return firstName + " | " + secondName + " | " + date;
    }

    public int getMouth() {
        return date.getMonthValue();
    }
}
