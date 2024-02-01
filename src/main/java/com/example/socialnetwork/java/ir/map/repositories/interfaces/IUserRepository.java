package com.example.socialnetwork.java.ir.map.repositories.interfaces;

import com.example.socialnetwork.java.ir.map.domain.DTO_FriendsOfUser;
import com.example.socialnetwork.java.ir.map.domain.Entity;
import com.example.socialnetwork.java.ir.map.domain.Status;
import com.example.socialnetwork.java.ir.map.domain.User;
import com.example.socialnetwork.java.ir.map.repositories.interfaces.IRepository;

import java.util.ArrayList;

/**
 * Interface for User Repository that extends the generic CRUD operations repository
 * interface for User entities specifically, adding social networking features.
 */
public interface IUserRepository<ID, E extends Entity<ID>> extends IRepository<ID, E> {
    /**
     * Gets an iterable of DTO_FriendsOfUser objects for a given user ID.
     *
     * @param id The user ID for which to find friends.
     * @return An Iterable of DTO_FriendsOfUser objects.
     */
    Iterable<DTO_FriendsOfUser> getFriendsOfUser(Long id);

    /**
     * Gets a list of User entities who are friends of the user with the given ID.
     *
     * @param id The user ID for which to find friends.
     * @return An ArrayList of User entities.
     */
    ArrayList<User> getFriendsOfUser2(Long id);

    /**
     * Gets a list of User entities who are friends of the user with the given ID and have a specific status.
     *
     * @param id The user ID for which to find friends.
     * @param status The status of the friendship.
     * @return An ArrayList of User entities.
     */
    ArrayList<User> getFriendsOfUserWithStatus(Long id, Status status);

    /**
     * Gets a list of User entities for whom the user with the given ID has received friend requests.
     *
     * @param id The user ID who received friend requests.
     * @return An ArrayList of User entities.
     */
    ArrayList<User> getUsersForReceived(Long id);

    /**
     * Gets a list of User entities to whom the user with the given ID has sent friend requests.
     *
     * @param id The user ID who sent friend requests.
     * @return An ArrayList of User entities.
     */
    ArrayList<User> getUsersForSent(Long id);
}

