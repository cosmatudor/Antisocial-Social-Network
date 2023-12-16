package com.example.socialnetwork.java.ir.map.repositories.paging;

import java.util.stream.Stream;

public class Page<T> implements IPage<T> {
    private IPageable pageable;
    private Stream<T> content;

    public Page(IPageable pageable, Stream<T> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public IPageable getPageable() {
        return this.pageable;
    }

    @Override
    public IPageable nextPageable() {
        return new Pageable(this.pageable.getPageNumber() + 1, this.pageable.getPageSize());
    }

    @Override
    public Stream<T> getContent() {
        return this.content;
    }
}
