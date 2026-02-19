package com.example.springdb.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserPutRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotBlank
        String email,
        @NotNull
        int age,
        @NotNull
        LocalDate hireDate,
        @NotBlank
        String phoneNumber,
        @NotNull
        String password
){}

