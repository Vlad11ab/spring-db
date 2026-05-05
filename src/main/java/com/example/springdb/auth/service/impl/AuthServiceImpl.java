package com.example.springdb.auth.service.impl;

import com.example.springdb.auth.dtos.AuthLoginRequest;
import com.example.springdb.auth.dtos.AuthResponse;
import com.example.springdb.auth.service.AuthService;
import com.example.springdb.config.jwt.JWTTokenProvider;
import com.example.springdb.config.security.UserPermissions;
import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserCreateResponse;
import com.example.springdb.exceptions.EmailAlreadyExistsException;
import com.example.springdb.model.UserApp;
import com.example.springdb.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JWTTokenProvider jwtTokenProvider,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserCreateResponse register(UserCreateRequest request) {
        if(userRepository.existsByEmailJPQL(request.email())){
            throw new EmailAlreadyExistsException();
        }

        UserApp userApp = new UserApp();
        userApp.setFirstName(request.firstName());
        userApp.setLastName(request.lastName());
        userApp.setEmail(request.email());
        userApp.setAge(request.age());
        userApp.setHireDate(request.hireDate());
        userApp.setPhoneNumber(request.phoneNumber());
        userApp.setPassword(this.passwordEncoder.encode(request.password()));
        userApp.setPermissions(Set.of(UserPermissions.USER_READ,UserPermissions.USER_WRITE));

        UserApp savedUserApp = userRepository.save(userApp);
        return new UserCreateResponse(
                savedUserApp.getId(),
                savedUserApp.getFirstName(),
                savedUserApp.getLastName(),
                savedUserApp.getEmail(),
                savedUserApp.getAge(),
                savedUserApp.getHireDate(),
                savedUserApp.getPhoneNumber(),
                jwtTokenProvider.generateToken(savedUserApp)
        );
    }

    @Override
    public AuthResponse login(AuthLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserApp user = userRepository.findByEmailIgnoreCaseJPQL(request.email())
                .orElseThrow(() -> new UsernameNotFoundException(request.email()));

        return new AuthResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAge(),
                user.getHireDate(),
                user.getPhoneNumber(),
                user.getPermissions(),
                jwtTokenProvider.generateToken(user)
        );
    }
}
