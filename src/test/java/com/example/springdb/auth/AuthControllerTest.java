package com.example.springdb.auth;

import com.example.springdb.auth.controllers.AuthController;
import com.example.springdb.auth.dtos.AuthLoginRequest;
import com.example.springdb.auth.dtos.AuthResponse;
import com.example.springdb.auth.service.AuthService;
import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserCreateResponse;
import com.example.springdb.system.exceptions.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private final AuthService authService = mock(AuthService.class);
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(authService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void registerReturnsCreatedResponseBody() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "Vlad",
                "Breazu",
                "vlad@example.com",
                22,
                LocalDate.of(2026, 5, 5),
                "0783748374",
                "parola123"
        );

        UserCreateResponse response = new UserCreateResponse(
                15L,
                "Vlad",
                "Breazu",
                "vlad@example.com",
                22,
                LocalDate.of(2026, 5, 5),
                "0783748374",
                "jwt-token"
        );

        when(authService.register(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(15L))
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void loginReturnsJwtResponseBody() throws Exception {
        AuthLoginRequest request = new AuthLoginRequest("vlad@example.com", "parola123");
        AuthResponse response = new AuthResponse(
                15L,
                "Vlad",
                "Breazu",
                "vlad@example.com",
                22,
                LocalDate.of(2026, 5, 5),
                "0783748374",
                null,
                "jwt-token"
        );

        when(authService.login(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("vlad@example.com"))
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void registerReturnsBadRequestForInvalidPayload() throws Exception {
        String invalidPayload = """
                {
                  "firstName": "V",
                  "lastName": "Br",
                  "email": "invalid",
                  "age": 22,
                  "hireDate": "2026-05-05",
                  "phoneNumber": "123",
                  "password": "abc"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
}
