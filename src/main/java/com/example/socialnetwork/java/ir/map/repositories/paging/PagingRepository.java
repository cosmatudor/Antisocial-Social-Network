package com.example.socialnetwork.java.ir.map.repositories.paging;

import com.example.socialnetwork.java.ir.map.domain.Entity;
import com.example.socialnetwork.java.ir.map.repositories.DBRepositories.UserDBRepository;
import com.example.socialnetwork.java.ir.map.repositories.IRepository;

import java.util.Optional;

public interface PagingRepository<ID, E extends Entity<ID>> extends IRepository<ID, E> {

    Page<E> findAll(Pageable pageable);

}