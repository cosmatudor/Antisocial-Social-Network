package com.example.socialnetwork.java.ir.map.repositories.paging;

public class Pageable implements IPageable {

    private int pageNumber;
    private int pageSize;

    public Pageable(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }
}