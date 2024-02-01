package com.example.socialnetwork.java.ir.map.domain;

import java.util.ArrayList;
import java.util.Objects;

public class User extends Entity<Long> {

    private String username;
    private String password;
    private String firstName;
    private String secondName;
    private ArrayList<User> friends = new ArrayList<>();

    public User(long id, String username, String password, String firstName, String secondName) {
        super(id);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    // -------------- Getters --------------
    /**
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the user's second name
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * @return the user's friends
     */
    public ArrayList<User> getFriends() {
        return friends;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // -------------- Setters --------------
    /**
     * sets the user's first name
     * @param firstName - the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * sets the user's second name
     * @param secondName - the new second name
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * sets the user's friends
     * @param friends - the new friends
     */
    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // -------------- Overrides --------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(secondName, user.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, firstName, secondName);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName +
                '}';
    }
}
