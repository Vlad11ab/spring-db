package com.example.springdb.service.query;

import com.example.springdb.dtos.UserResponse;
import com.example.springdb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<UserResponse> findAllUsers();
    Optional<UserResponse> findByFirstName(String firstName);
    Optional<UserResponse> findByLastName(String lastName);
    Optional<UserResponse> findByEmail(String email);
    Optional<UserResponse> findByEmailIgnoreCase(String email);
    List<UserResponse> findByAgeRange(int minAge, int maxAge);
    List<UserResponse> findHiredBetween(LocalDate from, LocalDate to);
    Page<UserResponse> searchByFirstNameOrEmailOrPhoneNumber(String query, Pageable pageable);
    List<UserResponse> search(String firstName, String lastName, String email, Integer age, LocalDate hireDate, String password, String phoneNumber);
    long countHiredBefore(LocalDate date);
    boolean userExistsByEmail(String email);
}
