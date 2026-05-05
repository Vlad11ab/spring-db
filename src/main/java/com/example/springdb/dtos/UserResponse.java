package com.example.springdb.dtos;

import com.example.springdb.config.security.UserPermissions;

import java.time.LocalDate;
import java.util.Set;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Integer age,
        LocalDate hireDate,
        String phoneNumber,
        Set<UserPermissions> userPermissions, String password
){}
