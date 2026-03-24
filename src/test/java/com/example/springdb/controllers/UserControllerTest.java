package com.example.springdb.controllers;


import com.example.springdb.controller.UserController;
import com.example.springdb.dtos.*;
import com.example.springdb.exceptions.EmailAlreadyExistsException;
import com.example.springdb.exceptions.UserNotFoundException;
import com.example.springdb.model.User;
import com.example.springdb.service.command.UserCommandService;
import com.example.springdb.service.query.UserQueryService;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserQueryService userQueryService;

    @MockitoBean
    private UserCommandService userCommandService;
    @Autowired
    private ObjectMapper objectMapper;

    User user1 = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).build();
    User user2 = User.builder().id(3L).firstName("Maria").lastName("Ionescu").email("maria.ionescu@gmail.com").age(28).build();
    User user3 = User.builder().id(4L).firstName("Alex").lastName("Georgescu").email("alex.georgescu@gmail.com").age(31).hireDate(LocalDate.now()).password("alexPass").phoneNumber("0743987654").build();
    User user4 = User.builder().id(5L).firstName("Elena").lastName("Dumitrescu").email("elena.dumitrescu@gmail.com").age(24).hireDate(LocalDate.now()).password("elenaPwd").phoneNumber("0755123987").build();
    User user5 = User.builder().id(6L).firstName("Radu").lastName("Stan").email("radu.stan@gmail.com").age(29).hireDate(LocalDate.now()).password("raduPass").phoneNumber("0766234567").build();
    UserResponse userResponse1 = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");
    UserResponse userResponse2 = new UserResponse(3L, "Maria", "Ionescu", "maria.ionescu@gmail.com", 28, LocalDate.now(), "0732456789", "securePass");
    UserResponse userResponse3 = new UserResponse(4L, "Alex", "Georgescu", "alex.georgescu@gmail.com", 31, LocalDate.now(), "0743987654", "alexPass");
    UserResponse userResponse4 = new UserResponse(5L, "Elena", "Dumitrescu", "elena.dumitrescu@gmail.com", 24, LocalDate.now(), "0755123987", "elenaPwd");
    UserResponse userResponse5 = new UserResponse(6L, "Radu", "Stan", "radu.stan@gmail.com", 29, LocalDate.now(), "0766234567", "raduPass");




    @Test
    void getAllReturnsList() throws Exception{
        when(userQueryService.findAllUsers()).thenReturn(List.of(userResponse1,userResponse2,userResponse3,userResponse4,userResponse5));

        MvcResult result = mockMvc.perform(get("/api/v1/users/all"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getByAgeRangeReturnsList() throws Exception{
        UserResponseList userResponseList = new UserResponseList(List.of(
                userResponse1,
                userResponse2
        ));


        when(userQueryService.findByAgeRange(20,30)).thenReturn(userResponseList);

        mockMvc.perform(get("/api/v1/users/age/20-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.userResponseList.length()").value(2))
                .andExpect(jsonPath("$.userResponseList[0].id").value(2L))
                .andExpect(jsonPath("$.userResponseList[0].firstName").value("Andrei"))
                .andExpect(jsonPath("$.userResponseList[0].lastName").value("Popescu"))
                .andExpect(jsonPath("$.userResponseList[0].email").value("andrei.popescu@gmail.com"))
                .andExpect(jsonPath("$.userResponseList[0].age").value(25))
                .andExpect(jsonPath("$.userResponseList[1].id").value(3L))
                .andExpect(jsonPath("$.userResponseList[1].firstName").value("Maria"))
                .andExpect(jsonPath("$.userResponseList[1].lastName").value("Ionescu"))
                .andExpect(jsonPath("$.userResponseList[1].age").value(28));

        verify(userQueryService).findByAgeRange(20,30);
        verifyNoInteractions(userCommandService);
                }

    @Test
    void getByEmailReturnsUser() throws Exception{
        when(userQueryService.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(userResponse1));

        mockMvc.perform(get("/api/v1/users/email/andrei.popescu@gmail.com"))
                .andExpect(status().isOk());



    }

    @Test
    void createReturns201() throws Exception{
        UserCreateRequest request = new UserCreateRequest("Vlad","Breazu","breazuvlad@gmail.com",22, LocalDate.now(),"0783748374","parola");
        UserResponse userResponse = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");

        when(userCommandService.create(request)).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/users/add")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.firstName").value("Andrei"))
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.email").value("andrei.popescu@gmail.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.hireDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.phoneNumber").value("0721123456"))
                .andExpect(jsonPath("$.password").value("parola123"));

                verify(userCommandService).create(request);
    }

    @Test
    void patchReturnsOk() throws Exception {
        UserPatchRequest request = new UserPatchRequest(22,"breazuvlad@gmail.com","parola");
        UserResponse userResponse = new UserResponse(3L, "Radu", "Stan", "breazuvlad@gmail.com", 22, LocalDate.now(), "073827210", "parola");

        when(userCommandService.patch(3L,request)).thenReturn(userResponse);

        mockMvc.perform(patch("/api/v1/users/edit/3")
                       .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                       .content(objectMapper.writeValueAsString(request)))
                       .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.firstName").value("Radu"))
                .andExpect(jsonPath("$.lastName").value("Stan"))
                .andExpect(jsonPath("$.email").value("breazuvlad@gmail.com"))
                .andExpect(jsonPath("$.age").value(22))
                .andExpect(jsonPath("$.hireDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.phoneNumber").value("073827210"))
                .andExpect(jsonPath("$.password").value("parola"));

                verify(userCommandService).patch(3L,request);

    }

    @Test
    void deleteReturnsNoContent() throws Exception{
        mockMvc.perform(delete("/api/v1/users/delete/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void search() throws Exception{
        User user1 = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).build();
        User user2 = User.builder().id(3L).firstName("Maria").lastName("Ionescu").email("maria.ionescu@gmail.com").age(28).build();
        UserResponse userResponse1 = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");
        UserResponse userResponse2 = new UserResponse(3L, "Maria", "Ionescu", "maria.ionescu@gmail.com", 28, LocalDate.now(), "0732456789", "securePass");

        UserResponseList userResponseList = new UserResponseList(List.of(userResponse1,userResponse2));
        String firstNameSearch = "Andrei";
        String lastNameSearch = "Popescu";
        String emailSearch = "andrei.popescu@gmail.com";
        Integer ageSearch = 25;
        LocalDate dateSearch = LocalDate.now();
        String passwordSearch = "parola123";
        String phoneSearch = "0721123456";

        when(userQueryService.search(firstNameSearch,lastNameSearch,emailSearch,ageSearch,LocalDate.now(),passwordSearch,phoneSearch)).thenReturn(userResponseList);

        mockMvc.perform(get("/api/v1/users/search")
                .param("firstName", firstNameSearch)
                .param("lastName", lastNameSearch)
                .param("email", emailSearch)
                .param("age", String.valueOf(ageSearch))
                .param("hireDate", String.valueOf(dateSearch))
                .param("password", passwordSearch)
                .param("phoneNumber", phoneSearch)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userResponseList[0].id").value(2L))
                .andExpect(jsonPath("$.userResponseList[0].firstName").value("Andrei"))
                .andExpect(jsonPath("$.userResponseList[0].lastName").value("Popescu"))
                .andExpect(jsonPath("$.userResponseList[0].email").value("andrei.popescu@gmail.com"))
                .andExpect(jsonPath("$.userResponseList[0].age").value(25))
                .andExpect(jsonPath("$.userResponseList[0].hireDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.userResponseList[0].phoneNumber").value("0721123456"))
                .andExpect(jsonPath("$.userResponseList[0].password").value("parola123"));

                verify(userQueryService).search(firstNameSearch,lastNameSearch,emailSearch,ageSearch,dateSearch,passwordSearch,phoneSearch);
    }

    @Test
    void searchByFirstNameReturnsOk() throws Exception{
        UserResponse userResponse = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");

        UserResponseList userResponseList = new UserResponseList(List.of(userResponse));
        String searchText = "Andrei";

        when(userQueryService.searchByFirstNameOrEmailOrPhoneNumber(searchText, Pageable.unpaged())).thenReturn(userResponseList);

        mockMvc.perform(get("/api/v1/users/searchByFirstName")
                .param("firstName", searchText)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userResponseList[0].id").value(2L))
                .andExpect(jsonPath("$.userResponseList[0].firstName").value("Andrei"))
                .andExpect(jsonPath("$.userResponseList[0].lastName").value("Popescu"))
                .andExpect(jsonPath("$.userResponseList[0].email").value("andrei.popescu@gmail.com"))
                .andExpect(jsonPath("$.userResponseList[0].age").value(25))
                .andExpect(jsonPath("$.userResponseList[0].hireDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.userResponseList[0].phoneNumber").value("0721123456"))
                .andExpect(jsonPath("$.userResponseList[0].password").value("parola123"));

                verify(userQueryService).searchByFirstNameOrEmailOrPhoneNumber(searchText,Pageable.unpaged());
    }

    @Test
    void updateReturnsUpdatedResponse() throws Exception {
        UserPutRequest request = new UserPutRequest("Vlad","Breazu","breazuvlad@gmail.com",22, LocalDate.now(),"0783748374","parola");
        UserResponse userResponse = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");

        when(userCommandService.update(2L,request)).thenReturn(userResponse);

        mockMvc.perform(put("/api/v1/users/edit/update/{userId}",userResponse.id())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.firstName").value("Andrei"))
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.email").value("andrei.popescu@gmail.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.hireDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.phoneNumber").value("0721123456"))
                .andExpect(jsonPath("$.password").value("parola123"));

                verify(userCommandService).update(2L,request);
                verifyNoInteractions(userQueryService);
    }

    @Test
    void createCarReturnsConflictWhenServiceThrowsAlreadyExists() throws Exception {
        UserCreateRequest request = new UserCreateRequest( "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");

        when(userCommandService.create(request)).thenThrow(new EmailAlreadyExistsException());

        mockMvc.perform(post("/api/v1/users/add")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("EMAIL_ALREADY_EXISTS_EXCEPTION"));
    }

    @Test
    void deleteUserReturnsConflictWhenServiceThrowsNotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(userCommandService).delete(10L);

        mockMvc.perform(delete("/api/v1/users/delete/{userId}", 10L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT FOUND"))
                .andExpect(jsonPath("$.message").value("USER_NOT_FOUND_EXCEPTION"));
    }






}
