package com.example.springdb.mappers;

import com.example.springdb.config.security.UserPermissions;
import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserCreateResponse;
import com.example.springdb.dtos.UserResponse;
import com.example.springdb.model.UserApp;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;


public class UserMapper {

    public static UserApp toEntity(UserCreateRequest request){
        if(request == null) return null;

        return UserApp.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .age(request.age())
                .hireDate(request.hireDate())
                .phoneNumber(request.phoneNumber())
                .password(request.password())
                .build();
    }

    public static UserResponse toDto(UserApp userApp){

        return new UserResponse(
                userApp.getId(),
                userApp.getFirstName(),
                userApp.getLastName(),
                userApp.getEmail(),
                userApp.getAge(),
                userApp.getHireDate(),
                userApp.getPhoneNumber(),
                copyPermissions(userApp.getPermissions()), userApp.getPassword()
        );
    }



    private static Set<UserPermissions> copyPermissions(Set<UserPermissions> permissions) {
        if (permissions == null || permissions.isEmpty()){
            return Collections.emptySet();
        }
        return EnumSet.copyOf(permissions);
    }
}
