package com.example.springdb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyUpdateRequestException extends RuntimeException {
    public EmptyUpdateRequestException() {
        super("EMPTY_UPDATE_REQUEST_EXCEPTION");
    }
}
