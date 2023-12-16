package com.example.socialnetwork.java.ir.map.repositories.paging;

import java.util.stream.Stream;

public interface IPage<E> {
    IPageable getPageable();

    IPageable nextPageable();

    Stream<E> getContent();
}
