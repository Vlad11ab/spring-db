package com.example.springdb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyPatchRequest extends RuntimeException {
    public EmptyPatchRequest() {
        super("EMPTY_PATCH_REQUEST");
    }
}
