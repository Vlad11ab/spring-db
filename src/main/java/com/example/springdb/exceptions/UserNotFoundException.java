package com.example.springdb.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("USER_NOT_FOUND_EXCEPTION");
    }
}
