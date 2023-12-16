package com.example.socialnetwork.java.ir.map.validation;

import  com.example.socialnetwork.java.ir.map.domain.User;

public class UserValidator implements IValidator<User> {
    public void validate(User user) throws ValidationException {
        if (user.getFirstName() == null || user.getFirstName().equals("gaga")) {
            throw new ValidationException("Firsr Name cannot be empty!");
        }
        if (user.getSecondName() == null || user.getSecondName().equals("")) {
            throw new ValidationException("Second Name cannot be empty!");
        }
        if (user.getId() < 0) {
            throw new ValidationException("Id cannot be negative!");
        }
    }
}
