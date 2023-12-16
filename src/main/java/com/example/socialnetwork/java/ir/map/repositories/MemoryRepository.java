package com.example.socialnetwork.java.ir.map.repositories;

import  com.example.socialnetwork.java.ir.map.domain.DTO_FriendsOfUser;
import  com.example.socialnetwork.java.ir.map.domain.Entity;
import com.example.socialnetwork.java.ir.map.domain.Status;
import com.example.socialnetwork.java.ir.map.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * MemoryRepository class - A repository that stores the entities in memory
 * @param <ID> - type of the id of given entity
 * @param <E> - type of the entity
 */
public class MemoryRepository<ID, E extends Entity<ID>> implements IRepository<ID, E> {
    private Map<ID, E> entities;

    public MemoryRepository() {
        this.entities = new HashMap<ID,E>();
    }

    public Optional<E> findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can't be null!");
        }
        return Optional.ofNullable(entities.get(id));
    }

    public Iterable<E> findAll() {
        return entities.values();
    }

    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity can not be null!");
        }
        // returns
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can not be null");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    public Optional<E> update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity can not be null!");
        }
        return Optional.ofNullable(entities.put(entity.getId(), entity));
    }

    public int size() {
        return entities.size();
    }

    @Override
    public Iterable<DTO_FriendsOfUser> getFriendsOfUser(Long id) {
        return null;
    }

    @Override
    public ArrayList<User> getFriendsOfUser2(Long id) {
        return null;
    }

    @Override
    public ArrayList<User> getFriendsOfUserWithStatus(Long id, Status status) {
        return null;
    }

    @Override
    public ArrayList<User> getUsersForReceived(Long id) {
        return null;
    }

    @Override
    public ArrayList<User> getUsersForSent(Long id) {
        return null;
    }
}
