package com.example.springdb.service.command.impl;

import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserPatchRequest;
import com.example.springdb.dtos.UserPutRequest;
import com.example.springdb.dtos.UserResponse;
import com.example.springdb.exceptions.EmailAlreadyExistsException;
import com.example.springdb.exceptions.UserNotFoundException;
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
    UserMapper userMapper;

    public UserCommandServiceImpl(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest request) {
        if(userRepository.existsByEmailJPQL(request.email())){
            throw new EmailAlreadyExistsException(request.email());
        }
        User savedUser = userRepository.save(userMapper.toEntity(request));
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponse patch(Long userId, UserPatchRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

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
        return userMapper.toDto(patchedUser);
    }

    @Override
    public UserResponse update(Long userId, UserPutRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setAge(45);
        user.setHireDate(LocalDate.now());
        user.setPhoneNumber(request.phoneNumber());
        user.setPassword(request.password());

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }
}
