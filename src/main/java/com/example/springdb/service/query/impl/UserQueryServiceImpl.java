package com.example.springdb.service.query.impl;

import com.example.springdb.dtos.UserResponse;
import com.example.springdb.mappers.UserMapper;
import com.example.springdb.model.User;
import com.example.springdb.repository.UserRepository;
import com.example.springdb.service.query.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.sql.Types.NULL;

@Component
public class UserQueryServiceImpl implements UserQueryService {

    UserRepository userRepository;
    UserMapper userMapper;

    public UserQueryServiceImpl(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;

    }

    @Override
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
//          .map(el->userMapper.toDto(el))
    }

    @Override
    public Optional<UserResponse> findByFirstName(String firstName) {
        return userRepository.findByLastNameIgnoreCaseJPQL(firstName)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserResponse> findByLastName(String lastName) {
        return userRepository.findByLastNameIgnoreCaseJPQL(lastName)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserResponse> findByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCaseJPQL(email)
                .map(userMapper::toDto);
    }

    @Override
    public List<UserResponse> findByAgeRange(int minAge, int maxAge) {
        return userRepository.findByAgeRange(minAge,maxAge)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserResponse> findHiredBetween(LocalDate from, LocalDate to) {
        return userRepository.findHiredBetween(from,to)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserResponse> search(String firstName, String lastName, String email, Integer age, LocalDate hireDate, String password, String phoneNumber) {

        var list = userRepository.findAll().stream()
                .filter(user -> firstName == null || firstName.equals(user.getFirstName()))
                .filter(user -> lastName == null || lastName.equals(user.getLastName()))
                .filter(user -> email == null || email.equals(user.getEmail()))
                .filter(user -> age == null || age == user.getAge())
                .filter(user -> hireDate == null || hireDate.equals(user.getHireDate()))
                .filter(user -> password == null || password.equals(user.getPassword()))
                .map(userMapper::toDto)
                .toList();

        return list;
    }

    public Page<UserResponse> searchByFirstNameOrEmailOrPhoneNumber(String query, Pageable pageable) {
        return userRepository.searchByFirstNameOrEmailOrPhoneNumber(query,pageable)
                .map(userMapper::toDto);
    }

    @Override
    public long countHiredBefore(LocalDate date) {
        return userRepository.countHiredBefore(date);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmailJPQL(email);
    }
}
