package com.example.socialnetwork.java.ir.map.validation;

@FunctionalInterface
public interface IValidator<T> {
    void validate(T entity) throws ValidationException;
}
