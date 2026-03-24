package com.example.springdb.service.query;

import com.example.springdb.dtos.UserResponse;
import com.example.springdb.dtos.UserResponseList;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<UserResponse> findAllUsers();
    Optional<UserResponse> findByFirstName(String firstName);
    Optional<UserResponse> findByLastName(String lastName);
    Optional<UserResponse> findByEmail(String email);
    Optional<UserResponse> findByEmailIgnoreCase(String email);
    UserResponseList findByAgeRange(int minAge, int maxAge);
    List<UserResponse> findHiredBetween(LocalDate from, LocalDate to);
    UserResponseList searchByFirstNameOrEmailOrPhoneNumber(String query, Pageable pageable);
    UserResponseList search(String firstName, String lastName, String email, Integer age, LocalDate hireDate, String password, String phoneNumber);
    long countHiredBefore(LocalDate date);
    boolean userExistsByEmail(String email);
}
