package com.example.springdb.dtos;

public record UserPatchRequest(
        int age,
        String email,
        String password

){}
