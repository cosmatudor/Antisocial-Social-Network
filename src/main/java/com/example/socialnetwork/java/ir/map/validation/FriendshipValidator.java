package com.example.socialnetwork.java.ir.map.validation;

import  com.example.socialnetwork.java.ir.map.domain.Friendship;

public class FriendshipValidator implements IValidator<Friendship> {
    public void validate(Friendship friendship) throws ValidationException {
        if (friendship.getUser1() == friendship.getUser2()) {
            throw new ValidationException("You cannot be friends with yourself!");
        }
    }
}
