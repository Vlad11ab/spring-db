package com.example.springdb.auth.controllers;

import com.example.springdb.auth.service.AuthService;
import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserCreateResponse;
import com.example.springdb.service.command.UserCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Public endpoints: register, login")
public class AuthController {

    private final AuthService authService;
    private final UserCommandService userCommandService;

    public AuthController(AuthService authService, UserCommandService userCommandService) {
        this.authService = authService;
        this.userCommandService = userCommandService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserCreateRequest request){
        log.info("HTTP POST /api/v1/auth/register");

        UserCreateResponse userCreateResponse=authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
