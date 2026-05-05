package com.example.springdb.auth;

import com.example.springdb.auth.dtos.AuthLoginRequest;
import com.example.springdb.auth.dtos.AuthResponse;
import com.example.springdb.auth.service.impl.AuthServiceImpl;
import com.example.springdb.config.jwt.JWTTokenProvider;
import com.example.springdb.config.security.UserPermissions;
import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserCreateResponse;
import com.example.springdb.exceptions.EmailAlreadyExistsException;
import com.example.springdb.model.UserApp;
import com.example.springdb.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authenticationManager, jwtTokenProvider, userRepository, passwordEncoder);
    }

    @Test
    void registerReturnsCreatedUserWithTokenAndEncodedPassword() {
        UserCreateRequest request = new UserCreateRequest(
                "Vlad",
                "Breazu",
                "vlad@example.com",
                22,
                LocalDate.of(2026, 5, 5),
                "0783748374",
                "parola123"
        );

        UserApp savedUser = UserApp.builder()
                .id(10L)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .age(request.age())
                .hireDate(request.hireDate())
                .phoneNumber(request.phoneNumber())
                .password("{bcrypt}encoded-password")
                .permissions(Set.of(UserPermissions.USER_READ, UserPermissions.USER_WRITE))
                .build();

        when(userRepository.existsByEmailJPQL(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("{bcrypt}encoded-password");
        when(userRepository.save(any(UserApp.class))).thenReturn(savedUser);
        when(jwtTokenProvider.generateToken(savedUser)).thenReturn("jwt-token");

        UserCreateResponse response = authService.register(request);

        ArgumentCaptor<UserApp> userCaptor = ArgumentCaptor.forClass(UserApp.class);
        verify(userRepository).save(userCaptor.capture());
        UserApp persistedUser = userCaptor.getValue();

        assertEquals("{bcrypt}encoded-password", persistedUser.getPassword());
        assertTrue(persistedUser.getPermissions().contains(UserPermissions.USER_READ));
        assertTrue(persistedUser.getPermissions().contains(UserPermissions.USER_WRITE));
        assertEquals(10L, response.id());
        assertEquals("jwt-token", response.token());
    }

    @Test
    void registerThrowsConflictWhenEmailExists() {
        UserCreateRequest request = new UserCreateRequest(
                "Vlad",
                "Breazu",
                "vlad@example.com",
                22,
                LocalDate.of(2026, 5, 5),
                "0783748374",
                "parola123"
        );

        when(userRepository.existsByEmailJPQL(request.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    void loginAuthenticatesAndReturnsJwtPayload() {
        AuthLoginRequest request = new AuthLoginRequest("vlad@example.com", "parola123");
        UserApp user = UserApp.builder()
                .id(11L)
                .firstName("Vlad")
                .lastName("Breazu")
                .email("vlad@example.com")
                .age(22)
                .hireDate(LocalDate.of(2026, 5, 5))
                .phoneNumber("0783748374")
                .password("{bcrypt}encoded-password")
                .permissions(Set.of(UserPermissions.USER_READ))
                .build();

        when(userRepository.findByEmailIgnoreCaseJPQL(request.email())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        assertNotNull(response);
        assertEquals(11L, response.id());
        assertEquals("jwt-token", response.token());
        assertEquals(Set.of(UserPermissions.USER_READ), response.directPermissions());
    }
}
