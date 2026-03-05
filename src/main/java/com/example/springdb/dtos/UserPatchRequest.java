package com.example.springdb.dtos;

public record UserPatchRequest(
        Integer age,
        String email,
        String password

){}
