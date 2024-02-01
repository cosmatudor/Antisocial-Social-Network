package com.example.socialnetwork.java.ir.map.repositories.paging;

import com.example.socialnetwork.java.ir.map.domain.Entity;
import com.example.socialnetwork.java.ir.map.domain.Status;
import com.example.socialnetwork.java.ir.map.repositories.interfaces.IRepository;
import com.example.socialnetwork.java.ir.map.repositories.interfaces.IUserRepository;

public interface IPagingRepository<ID, E extends Entity<ID>> extends IUserRepository<ID, E> {

    Page<E> findAll(IPageable pageable);

    Page<E> getFriendsOfUserWithStatus(Long id, Status status, IPageable pageable);

    Page<E> getUsersForReceived(Long id, IPageable pageable);

    Page<E> getUsersForSent(Long id, IPageable pageable);
}