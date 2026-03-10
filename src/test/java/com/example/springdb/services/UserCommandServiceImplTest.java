package com.example.springdb.services;

import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserPatchRequest;
import com.example.springdb.dtos.UserResponse;
import com.example.springdb.exceptions.EmailAlreadyExistsException;
import com.example.springdb.exceptions.EmptyPatchRequest;
import com.example.springdb.exceptions.PhoneNumberAlreadyExistsException;
import com.example.springdb.exceptions.UserNotFoundException;
import com.example.springdb.mappers.UserMapper;
import com.example.springdb.model.User;
import com.example.springdb.repository.UserRepository;
import com.example.springdb.service.command.impl.UserCommandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserCommandServiceImpl userCommandServiceImpl;

    @BeforeEach
    void setUp(){
       userMapper = new UserMapper();
       userCommandServiceImpl = new UserCommandServiceImpl(userRepository,userMapper);
    }

    @Test
    void createThrowsEmailAlreadyExistsException(){
       UserCreateRequest testUser = new UserCreateRequest(
               "Vlad",
               "Breazu",
               "breazuvlad@gmail.com",
               22,
               LocalDate.now(),
               "0722937847",
               "parolInteliJ"
       );
       when(userRepository.existsByEmailJPQL(testUser.email())).thenReturn(true);
       assertThrows(EmailAlreadyExistsException.class,()->userCommandServiceImpl.create(testUser));
    }

    @Test
    void createThrowsPhoneNumberAlreadyExistsException(){
        UserCreateRequest testUser = new UserCreateRequest(
                "Rares",
                "Zorila",
                "zorila@yahoo.com",
                20,
                LocalDate.now(),
                "0788473827",
                "UnitedS"
        );
        when(userRepository.existsByPhoneNumber(testUser.phoneNumber())).thenReturn(true);
        assertThrows(PhoneNumberAlreadyExistsException.class,()->userCommandServiceImpl.create(testUser));
    }

    @Test
    void createPersitsAndReturn(){
        UserCreateRequest request = new UserCreateRequest(
                "Rares",
                "Zorila",
                "zorila@yahoo.com",
                20,
                LocalDate.now(),
                "0788473827",
                "UnitedS"
        );

        User toSave = userMapper.toEntity(request);
        User savedUser=userMapper.toEntity(request);
        savedUser.setId(1L);
        when(userRepository.existsByEmailJPQL(request.email())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(request.phoneNumber())).thenReturn(false);
        when(userRepository.save(toSave)).thenReturn(savedUser);

        UserResponse userResponse = userCommandServiceImpl.create(request);
        assertEquals(userResponse.id(),1L);
    }

    @Test
    void createThrowsWhenNotFoundException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userCommandServiceImpl.delete(1L));
    }

    @Test
    void patchThrowsWhenNotFoundException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userCommandServiceImpl.patch(1L, new UserPatchRequest(null,null,null)));
    }

    @Test
    void patchThrowsEmptyPatchRequest(){
        User existing = new User();
        existing.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        UserPatchRequest emptyRequest = new UserPatchRequest(null,null,null);

        assertThrows(EmptyPatchRequest.class,()->userCommandServiceImpl.patch(1L, emptyRequest));
    }

    @Test
    void patchUpdatesOnlyProvidedFields(){
        User existing = new User();
        existing.setId(1L);
        existing.setAge(55);
        existing.setEmail("email@email.text");
        existing.setPassword("parola");


        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        when(userRepository.save(existing)).thenReturn(existing);

        UserPatchRequest patch = new UserPatchRequest(60,null,"null");
        UserResponse response = userCommandServiceImpl.patch(1L, patch);

        assertEquals(existing.getAge(), response.age());
        assertEquals(existing.getEmail(), response.email());
        assertEquals(existing.getPassword(),response.password());
        verify(userRepository).save(existing);
    }

    @Test
    void patchUpdatesAllOptionalFields(){
        User existing = new User();
        existing.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        UserPatchRequest patch = new UserPatchRequest(60,"email@email.text","parola");
        UserResponse response = userCommandServiceImpl.patch(1L, patch);

        assertEquals(existing.getAge(), response.age());
        assertEquals(existing.getEmail(), response.email());
        assertEquals(existing.getPassword(),response.password());
        verify(userRepository).save(existing);
    }


}
