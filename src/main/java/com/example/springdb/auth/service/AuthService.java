package com.example.springdb.auth.service;

import com.example.springdb.auth.dtos.AuthLoginRequest;
import com.example.springdb.auth.dtos.AuthResponse;
import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserCreateResponse;

public interface AuthService {
    UserCreateResponse register(UserCreateRequest request);
    AuthResponse login(AuthLoginRequest request);
}
