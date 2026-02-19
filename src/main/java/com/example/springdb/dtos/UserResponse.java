package com.example.springdb.dtos;

import java.time.LocalDate;


public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        int age,
        LocalDate hireDate,
        String phoneNumber,
        String password
)
{}
