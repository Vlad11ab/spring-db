package com.example.springdb.controller;

import com.example.springdb.dtos.*;
import com.example.springdb.service.command.UserCommandService;
import com.example.springdb.service.query.UserQueryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private UserQueryService userQueryService;
    private UserCommandService userCommandService;

    public UserController(
            UserQueryService userQueryService,
            UserCommandService userCommandService){

        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<List<UserResponse>>getAllUsers(){
        log.info("HTTP Method GET All Users");
        return ResponseEntity.status(HttpStatus.OK).body(userQueryService.findAllUsers());
    }

    @GetMapping("/age/{minAge}-{maxAge}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserResponseList> getUsersByAgeRange(@PathVariable int minAge, @PathVariable int maxAge){
        log.info("HTTP GET /api/v1/users minAge={} maxAge{}",minAge ,maxAge );
        return ResponseEntity.status(HttpStatus.OK).body(userQueryService.findByAgeRange(minAge,maxAge));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<Optional<UserResponse>> getUserByEmail(@PathVariable String email){
        log.info("HTTP METHOD GET BY EMAIL");
        log.debug("HTTP GET /api/v1/users/{}", email);
        return ResponseEntity.status(HttpStatus.OK).body(userQueryService.findByEmail(email));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest user){
        log.info("HTTP POST /api/v1/users firstName={} lastName={} email ={} age={}", user.firstName(), user.lastName(),user.email(), user.age());
        return ResponseEntity.status(HttpStatus.CREATED).body(userCommandService.create(user));
    }

    @PatchMapping("/edit/{userId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<UserResponse> patchUser(@PathVariable Long userId, @Valid @RequestBody UserPatchRequest patched){
        log.info("HTTP PATCH /api/v1/users/{}", userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userCommandService.patch(userId,patched));
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        log.info("HTTP DELETE /api/v1/users/delete/{}", userId);
        userCommandService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserResponseList> search(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) LocalDate hireDate,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String phoneNumber
    ) {
        log.info("HTTP SEARCH METHOD");
        log.debug("HTTP GET /api/v1/users/search firstName={} lastName={} email={} age={} hireDate={} password={} phoneNumber={}",
                firstName, lastName, email, age, hireDate, password, phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).body(userQueryService.search(firstName,lastName,email,age,hireDate,password,phoneNumber));
    }

    @GetMapping("/searchByFirstName")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserResponseList> searchByFirstName(
            @RequestParam(required = false) String firstName
    ){
        log.info("HTTP GET - SEARCH BY FIRSTNAME METHOD");
        log.debug("HTTP GET /api/v1/users/search firstName={}", firstName);
        String text = "";
        text = firstName;

        UserResponseList result = userQueryService.searchByFirstNameOrEmailOrPhoneNumber(text, Pageable.unpaged());

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping("/edit/update/{userId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<UserResponse> update(@PathVariable Long userId, @Valid @RequestBody UserPutRequest updated){
        log.info("HTTP PUT /api/v1/users/{} firstName{}, lastName{}, email{}, age{}, hireDate{}, phoneNumber{}, password{}",
                userId, updated.firstName(),updated.lastName(),updated.email(),updated.age(),updated.hireDate(),updated.phoneNumber(),updated.password());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userCommandService.update(userId, updated));
    }






}

