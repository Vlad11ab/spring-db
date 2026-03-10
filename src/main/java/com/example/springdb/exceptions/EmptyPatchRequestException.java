package com.example.springdb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyPatchRequestException extends RuntimeException {
    public EmptyPatchRequestException() {
        super("EMPTY_PATCH_REQUEST");
    }
}
