package com.example.springdb.service.command.impl;

import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserPatchRequest;
import com.example.springdb.dtos.UserPutRequest;
import com.example.springdb.dtos.UserResponse;
import com.example.springdb.exceptions.*;
import com.example.springdb.mappers.UserMapper;
import com.example.springdb.model.UserApp;
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
        UserApp savedUserApp = userRepository.save(UserMapper.toEntity(request));
        return UserMapper.toDto(savedUserApp);
    }

    @Override
    @Transactional
    public UserResponse patch(Long userId, UserPatchRequest request) {
        UserApp userApp = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());


        if(request.password() == null && request.age() == null && request.email() == null){
            throw new EmptyPatchRequestException();
        }
        if(request.password() != null && !request.password().isBlank()){
            userApp.setPassword(request.password());
        }
        if(request.age()>0){
            userApp.setAge(request.age());
        }
        if(request.email() != null && !request.email().isBlank() && request.email().length()>5){
            userApp.setEmail(request.email());
        }

        UserApp patchedUserApp = userRepository.save(userApp);
        return UserMapper.toDto(patchedUserApp);
    }

    @Override
    public UserResponse update(Long userId, UserPutRequest request) {
        UserApp userApp = userRepository.findById(userId)
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
        userApp.setFirstName(request.firstName());
        userApp.setLastName(request.lastName());
        userApp.setEmail(request.email());
        userApp.setAge(request.age());
        userApp.setHireDate(LocalDate.now());
        userApp.setPhoneNumber(request.phoneNumber());
        userApp.setPassword(request.password());

        UserApp updatedUserApp = userRepository.save(userApp);
        return UserMapper.toDto(updatedUserApp);
    }

    @Override
    @Transactional
    public UserResponse delete(Long id) {
        UserApp userApp = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        UserApp deleted = userApp;
        userRepository.delete(userApp);
        return UserMapper.toDto(deleted);
    }
}
