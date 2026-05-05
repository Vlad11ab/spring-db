//package com.example.springdb.services;
//
//import com.example.springdb.dtos.UserCreateRequest;
//import com.example.springdb.dtos.UserPatchRequest;
//import com.example.springdb.dtos.UserPutRequest;
//import com.example.springdb.dtos.UserResponse;
//import com.example.springdb.exceptions.*;
//import com.example.springdb.model.User;
//import com.example.springdb.repository.UserRepository;
//import com.example.springdb.service.command.impl.UserCommandServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserCommandServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//    private UserCommandServiceImpl userCommandServiceImpl;
//
//    @BeforeEach
//    void setUp(){
//       userCommandServiceImpl = new UserCommandServiceImpl(userRepository);
//    }
//
//    @Test
//    void createThrowsEmailAlreadyExistsException(){
//       UserCreateRequest testUser = new UserCreateRequest(
//               "Vlad",
//               "Breazu",
//               "breazuvlad@gmail.com",
//               22,
//               LocalDate.now(),
//               "0722937847",
//               "parolInteliJ"
//       );
//       when(userRepository.existsByEmailJPQL(testUser.email())).thenReturn(true);
//       assertThrows(EmailAlreadyExistsException.class,()->userCommandServiceImpl.create(testUser));
//    }
//
//    @Test
//    void createThrowsPhoneNumberAlreadyExistsException(){
//        UserCreateRequest testUser = new UserCreateRequest(
//                "Rares",
//                "Zorila",
//                "zorila@yahoo.com",
//                20,
//                LocalDate.now(),
//                "0788473827",
//                "UnitedS"
//        );
//        when(userRepository.existsByPhoneNumber(testUser.phoneNumber())).thenReturn(true);
//        assertThrows(PhoneNumberAlreadyExistsException.class,()->userCommandServiceImpl.create(testUser));
//    }
//
//    @Test
//    void createPersitsAndReturn(){
//        UserCreateRequest request = new UserCreateRequest(
//                "Rares",
//                "Zorila",
//                "zorila@yahoo.com",
//                20,
//                LocalDate.now(),
//                "0788473827",
//                "UnitedS"
//        );
//        UserResponse expectedResult = new UserResponse(
//                1L,
//                "Rares",
//                "Zorila",
//                "zorila@yahoo.com",
//                20,
//                LocalDate.now(),
//                "0788473827",
//                copyPermissions(user.getPermissions()), "UnitedS");
//        User userSaved = new User(1L,
//                "Rares",
//                "Zorila",
//                "zorila@yahoo.com",
//                20,LocalDate.now(),
//                "UnitedS",
//                "0788473827");
//        User userToSave = new User(
//                "Rares",
//                "Zorila",
//                "zorila@yahoo.com",
//                20,LocalDate.now(),
//                "UnitedS",
//                "0788473827");
//
//        when(userRepository.existsByEmailJPQL(request.email())).thenReturn(false);
//        when(userRepository.existsByPhoneNumber(request.phoneNumber())).thenReturn(false);
//        when(userRepository.save(userToSave)).thenReturn(userSaved);
//
//        UserResponse actualResponse = userCommandServiceImpl.create(request);
//        assertEquals(expectedResult,actualResponse);
//    }
//
//    @Test
//    void deleteThrowsWhenMissing(){
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(UserNotFoundException.class,()->userCommandServiceImpl.delete(1L));
//    }
//
//    @Test
//    void deleteRemovesWhenExisting(){
//        User existing = new User();
//        existing.setId(30L);
//
//        when(userRepository.findById(30L)).thenReturn(Optional.of(existing));
//        userCommandServiceImpl.delete(30L);
//        verify(userRepository).delete(existing);
//    }
//
//
//
//    @Test
//    void patchThrowsWhenNotFoundException(){
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(UserNotFoundException.class,()->userCommandServiceImpl.patch(1L, new UserPatchRequest(null,null,null)));
//    }
//
//    @Test
//    void patchThrowsWhenEmptyPayload(){
//        User existing = new User();
//        existing.setId(1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
//
//        UserPatchRequest emptyRequest = new UserPatchRequest(null,null,null);
//
//        assertThrows(EmptyPatchRequestException.class,()->userCommandServiceImpl.patch(1L, emptyRequest));
//    }
//
//    @Test
//    void patchUpdatesOnlyProvidedFields(){
//        User existing = new User();
//        existing.setId(1L);
//        existing.setAge(55);
//        existing.setEmail("email@email.text");
//        existing.setPassword("parola");
//
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
//
//        when(userRepository.save(existing)).thenReturn(existing);
//
//        UserPatchRequest patch = new UserPatchRequest(60,null,"null");
//        UserResponse response = userCommandServiceImpl.patch(1L, patch);
//
//        assertEquals(existing.getAge(), response.age());
//        assertEquals(existing.getEmail(), response.email());
//        assertEquals(existing.getPassword(),response.password());
//        verify(userRepository).save(existing);
//    }
//
//    @Test
//    void patchUpdatesAllOptionalFields(){
//        User existing = new User();
//        existing.setId(1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
//        when(userRepository.save(existing)).thenReturn(existing);
//
//        UserPatchRequest patch = new UserPatchRequest(60,"email@email.text","parola");
//        UserResponse response = userCommandServiceImpl.patch(1L, patch);
//
//        assertEquals(60, response.age());
//        assertEquals("email@email.text", response.email());
//        assertEquals("parola",response.password());
//    }
//
//    @Test
//    void updateThrowsWhenNotFound(){
//        UserPutRequest update = new UserPutRequest(
//                "Rares",
//                "Zorila",
//                "zorila@yahoo.com",
//                20,
//                LocalDate.now(),
//                "0788473827",
//                "UnitedS"
//        );
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(UserNotFoundException.class,()->userCommandServiceImpl.update(1L, update));
//    }
//
//    @Test
//    void updateThrowsWhenEmptyPayload(){
//        User existing = new User();
//        existing.setId(1L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
//
//        UserPutRequest emptyUpdate = new UserPutRequest("","","",null,null,"","");
//
//        assertThrows(EmptyUpdateRequestException.class,()-> userCommandServiceImpl.update(1L, emptyUpdate));
//    }
//
//    @Test
//    void updateReplacesFields(){
//        //preconditia
//        User existing = new User();
//        existing.setId(1L);
//        existing.setFirstName("Cristi");
//        existing.setLastName("Gogan");
//        existing.setEmail("cristi@yahoo.com");
//        existing.setAge(22);
//        existing.setHireDate(LocalDate.now());
//        existing.setPhoneNumber("0784876374");
//        existing.setPassword("Parola");
//        UserPutRequest request = new UserPutRequest("Vlad","Breazu","unEmail@yahoo.com",22,LocalDate.now(),"0722836743","AltaParola");
//        UserResponse expectedResult = new UserResponse(1L,"Vlad","Breazu","unEmail@yahoo.com",22,LocalDate.now(),"0722836743", copyPermissions(user.getPermissions()), "AltaParola");
//        User toSave = new User(1L,"Vlad","Breazu","unEmail@yahoo.com",22,LocalDate.now(),"AltaParola","0722836743");
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
//        when(userRepository.save(toSave)).thenReturn(toSave);
//
//        //actiunea
//        UserResponse result= userCommandServiceImpl.update(1L,request);
//
//        //verificare
//        assertEquals(expectedResult,result);
//    }
//
//
//
//
//}
