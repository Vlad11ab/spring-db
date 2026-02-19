package com.example.springdb.service.command;

import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserPatchRequest;
import com.example.springdb.dtos.UserPutRequest;
import com.example.springdb.dtos.UserResponse;

public interface UserCommandService {
    UserResponse create(UserCreateRequest request);
    UserResponse patch(Long userId, UserPatchRequest request);
    UserResponse update(Long userId, UserPutRequest request);
    void delete(Long id);
}
