package com.example.springdb.service.command.impl;

import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserPatchRequest;
import com.example.springdb.dtos.UserPutRequest;
import com.example.springdb.dtos.UserResponse;
import com.example.springdb.exceptions.*;
import com.example.springdb.mappers.UserMapper;
import com.example.springdb.model.User;
import com.example.springdb.repository.UserRepository;
import com.example.springdb.service.command.UserCommandService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class UserCommandServiceImpl implements UserCommandService {

    UserRepository userRepository;

    public UserCommandServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest request) {
        if(userRepository.existsByEmailJPQL(request.email())){
            throw new EmailAlreadyExistsException();
        }
        if(userRepository.existsByPhoneNumber(request.phoneNumber())){
            throw new PhoneNumberAlreadyExistsException();
        }
        User savedUser = userRepository.save(UserMapper.toEntity(request));
        return UserMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponse patch(Long userId, UserPatchRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());


        if(request.password() == null && request.age() == null && request.email() == null){
            throw new EmptyPatchRequestException();
        }
        if(request.password() != null && !request.password().isBlank()){
            user.setPassword(request.password());
        }
        if(request.age()>0){
            user.setAge(request.age());
        }
        if(request.email() != null && !request.email().isBlank() && request.email().length()>5){
            user.setEmail(request.email());
        }
        User patchedUser = userRepository.save(user);
        return UserMapper.toDto(patchedUser);
    }

    @Override
    public UserResponse update(Long userId, UserPutRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException());

        if(request.firstName().isBlank() &&
                request.lastName().isBlank() &&
                request.email().isBlank() &&
                request.age() == null &&
                request.hireDate() == null &&
                request.phoneNumber().isBlank() &&
                request.password().isBlank()
        ) {
            throw new EmptyUpdateRequestException();
        }
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setAge(request.age());
        user.setHireDate(LocalDate.now());
        user.setPhoneNumber(request.phoneNumber());
        user.setPassword(request.password());

        User updatedUser = userRepository.save(user);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        User deleted = user;
        userRepository.delete(user);
        return UserMapper.toDto(deleted);
    }
}
