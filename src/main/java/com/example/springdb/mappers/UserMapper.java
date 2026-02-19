package com.example.springdb.mappers;

import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserResponse;
import com.example.springdb.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request){
        if(request == null) return null;

        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .age(request.age())
                .hireDate(request.hireDate())
                .phoneNumber(request.phoneNumber())
                .password(request.password())
                .build();
    }

    public UserResponse toDto(User user){

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAge(),
                user.getHireDate(),
                user.getPhoneNumber(),
                user.getPassword()
        );
    }
}
