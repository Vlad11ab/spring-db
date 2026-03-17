package com.example.springdb.services;

import com.example.springdb.dtos.UserResponse;
import com.example.springdb.model.User;
import com.example.springdb.repository.UserRepository;
import com.example.springdb.service.query.UserQueryService;
import com.example.springdb.service.query.impl.UserQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;
    private UserQueryService userQueryService;

    @BeforeEach
    void setUp(){
        userQueryService = new UserQueryServiceImpl(userRepository);
    }

    //1.preconditie
    //ce intra si ce iese
    //2.actiune
    //3.verificare

    @Test
    void findUsers(){

        User user1 = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).hireDate(LocalDate.now()).password("parola123").phoneNumber("0721123456").build();
        User user2 = User.builder().id(3L).firstName("Maria").lastName("Ionescu").email("maria.ionescu@gmail.com").age(28).hireDate(LocalDate.now()).password("securePass").phoneNumber("0732456789").build();
        User user3 = User.builder().id(4L).firstName("Alex").lastName("Georgescu").email("alex.georgescu@gmail.com").age(31).hireDate(LocalDate.now()).password("alexPass").phoneNumber("0743987654").build();
        User user4 = User.builder().id(5L).firstName("Elena").lastName("Dumitrescu").email("elena.dumitrescu@gmail.com").age(24).hireDate(LocalDate.now()).password("elenaPwd").phoneNumber("0755123987").build();
        User user5 = User.builder().id(6L).firstName("Radu").lastName("Stan").email("radu.stan@gmail.com").age(29).hireDate(LocalDate.now()).password("raduPass").phoneNumber("0766234567").build();
        UserResponse userResponse1 = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");
        UserResponse userResponse2 = new UserResponse(3L, "Maria", "Ionescu", "maria.ionescu@gmail.com", 28, LocalDate.now(), "0732456789", "securePass");
        UserResponse userResponse3 = new UserResponse(4L, "Alex", "Georgescu", "alex.georgescu@gmail.com", 31, LocalDate.now(), "0743987654", "alexPass");
        UserResponse userResponse4 = new UserResponse(5L, "Elena", "Dumitrescu", "elena.dumitrescu@gmail.com", 24, LocalDate.now(), "0755123987", "elenaPwd");
        UserResponse userResponse5 = new UserResponse(6L, "Radu", "Stan", "radu.stan@gmail.com", 29, LocalDate.now(), "0766234567", "raduPass");
        List<User> users = List.of(user1,user2,user3,user4,user5);
        List<UserResponse> expectedList = List.of(userResponse1,userResponse2,userResponse3,userResponse4,userResponse5);
        when(userRepository.findAll()).thenReturn(users);
        List<UserResponse> actualList = userQueryService.findAllUsers();
        assertEquals(expectedList,actualList);


    }

    @Test
    void findByLastName(){
        User user = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).hireDate(LocalDate.now()).password("parola123").phoneNumber("0721123456").build();
        Optional<UserResponse> expected = Optional.of(new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123"));
        when(userRepository.findByLastNameIgnoreCaseJPQL("Breazu")).thenReturn(Optional.ofNullable(user));
        Optional<UserResponse> actual = userQueryService.findByLastName("Breazu");
        assertEquals(expected,actual);
    }

    @Test
    void findByFirstName(){
        User user = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).hireDate(LocalDate.now()).password("parola123").phoneNumber("0721123456").build();
        Optional<UserResponse> expected = Optional.of(new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123"));
        when(userRepository.findByFirstNameIgnoreCaseJPQL("Andrei")).thenReturn(Optional.ofNullable(user));
        Optional<UserResponse> actual = userQueryService.findByFirstName("Andrei");
        assertEquals(expected,actual);
    }

    @Test
    void findByEmail(){
        User user = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).hireDate(LocalDate.now()).password("parola123").phoneNumber("0721123456").build();
        Optional<UserResponse> expected = Optional.of(new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123"));
        when(userRepository.findByEmailIgnoreCaseJPQL("andrei.popescu@gmail.com")).thenReturn(Optional.ofNullable(user));
        Optional<UserResponse> actual = userQueryService.findByEmailIgnoreCase("andrei.popescu@gmail.com");
        assertEquals(expected,actual);
    }

    @Test
    void findByAgeRange(){
        User user1 = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).hireDate(LocalDate.now()).password("parola123").phoneNumber("0721123456").build();
        User user2 = User.builder().id(3L).firstName("Maria").lastName("Ionescu").email("maria.ionescu@gmail.com").age(28).hireDate(LocalDate.now()).password("securePass").phoneNumber("0732456789").build();
        User user3 = User.builder().id(4L).firstName("Alex").lastName("Georgescu").email("alex.georgescu@gmail.com").age(31).hireDate(LocalDate.now()).password("alexPass").phoneNumber("0743987654").build();
        User user4 = User.builder().id(5L).firstName("Elena").lastName("Dumitrescu").email("elena.dumitrescu@gmail.com").age(24).hireDate(LocalDate.now()).password("elenaPwd").phoneNumber("0755123987").build();
        User user5 = User.builder().id(6L).firstName("Radu").lastName("Stan").email("radu.stan@gmail.com").age(29).hireDate(LocalDate.now()).password("raduPass").phoneNumber("0766234567").build();
        UserResponse userResponse1 = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");
        UserResponse userResponse2 = new UserResponse(3L, "Maria", "Ionescu", "maria.ionescu@gmail.com", 28, LocalDate.now(), "0732456789", "securePass");
        UserResponse userResponse3 = new UserResponse(4L, "Alex", "Georgescu", "alex.georgescu@gmail.com", 31, LocalDate.now(), "0743987654", "alexPass");
        UserResponse userResponse4 = new UserResponse(5L, "Elena", "Dumitrescu", "elena.dumitrescu@gmail.com", 24, LocalDate.now(), "0755123987", "elenaPwd");
        UserResponse userResponse5 = new UserResponse(6L, "Radu", "Stan", "radu.stan@gmail.com", 29, LocalDate.now(), "0766234567", "raduPass");

        List<User> users = List.of(user1,user2,user3,user4,user5);
        List<UserResponse> expected = List.of(userResponse3);

        when(userRepository.findByAgeRange(30,40)).thenReturn(List.of(user3));

        List<UserResponse> actual = userQueryService.findByAgeRange(30,40);
        assertEquals(expected, actual);
    }

    @Test
    void userExistsByEmail(){
        User user1 = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).hireDate(LocalDate.now()).password("parola123").phoneNumber("0721123456").build();
        User user2 = User.builder().id(3L).firstName("Maria").lastName("Ionescu").email("maria.ionescu@gmail.com").age(28).hireDate(LocalDate.now()).password("securePass").phoneNumber("0732456789").build();

        when(userRepository.existsByEmailJPQL("maria.ionescu@gmail.com")).thenReturn(true);

        boolean result = userQueryService.userExistsByEmail("maria.ionescu@gmail.com");

        assertTrue(result);
    }

    @Test
    void search(){
//        User user1 = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).hireDate(LocalDate.now()).password("parola123").phoneNumber("0721123456").build();
//        User user2 = User.builder().id(3L).firstName("Maria").lastName("Ionescu").email("maria.ionescu@gmail.com").age(28).hireDate(LocalDate.now()).password("securePass").phoneNumber("0732456789").build();
//        User user3 = User.builder().id(4L).firstName("Alex").lastName("Georgescu").email("alex.georgescu@gmail.com").age(31).hireDate(LocalDate.now()).password("alexPass").phoneNumber("0743987654").build();
//        User user4 = User.builder().id(5L).firstName("Elena").lastName("Dumitrescu").email("elena.dumitrescu@gmail.com").age(24).hireDate(LocalDate.now()).password("elenaPwd").phoneNumber("0755123987").build();
//        User user5 = User.builder().id(6L).firstName("Radu").lastName("Stan").email("radu.stan@gmail.com").age(29).hireDate(LocalDate.now()).password("raduPass").phoneNumber("0766234567").build();
//        UserResponse userResponse1 = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");
//        UserResponse userResponse2 = new UserResponse(3L, "Maria", "Ionescu", "maria.ionescu@gmail.com", 28, LocalDate.now(), "0732456789", "securePass");
//        UserResponse userResponse3 = new UserResponse(4L, "Alex", "Georgescu", "alex.georgescu@gmail.com", 31, LocalDate.now(), "0743987654", "alexPass");
//        UserResponse userResponse4 = new UserResponse(5L, "Elena", "Dumitrescu", "elena.dumitrescu@gmail.com", 24, LocalDate.now(), "0755123987", "elenaPwd");
//        UserResponse userResponse5 = new UserResponse(6L, "Radu", "Stan", "radu.stan@gmail.com", 29, LocalDate.now(), "0766234567", "raduPass");
//
//        List<User> users = List.of(user1,user2,user3,user4,user5);
//        List<UserResponse> expected = List.of(userResponse2);
//
//        when(userRepository.findAll().stream().filter(user -> user.getFirstName()
    }

    @Test
    void searchByFirstNameOrEmailOrPhoneNumber(){
        User user1 = User.builder().id(2L).firstName("Andrei").lastName("Popescu").email("andrei.popescu@gmail.com").age(25).hireDate(LocalDate.now()).password("parola123").phoneNumber("0721123456").build();
        User user2 = User.builder().id(3L).firstName("Maria").lastName("Ionescu").email("maria.ionescu@gmail.com").age(28).hireDate(LocalDate.now()).password("securePass").phoneNumber("0732456789").build();
        User user3 = User.builder().id(4L).firstName("Alex").lastName("Georgescu").email("alex.georgescu@gmail.com").age(31).hireDate(LocalDate.now()).password("alexPass").phoneNumber("0743987654").build();
        User user4 = User.builder().id(5L).firstName("Elena").lastName("Dumitrescu").email("elena.dumitrescu@gmail.com").age(24).hireDate(LocalDate.now()).password("elenaPwd").phoneNumber("0755123987").build();
        User user5 = User.builder().id(6L).firstName("Radu").lastName("Stan").email("radu.stan@gmail.com").age(29).hireDate(LocalDate.now()).password("raduPass").phoneNumber("0766234567").build();
        UserResponse userResponse1 = new UserResponse(2L, "Andrei", "Popescu", "andrei.popescu@gmail.com", 25, LocalDate.now(), "0721123456", "parola123");
        UserResponse userResponse2 = new UserResponse(3L, "Maria", "Ionescu", "maria.ionescu@gmail.com", 28, LocalDate.now(), "0732456789", "securePass");
        UserResponse userResponse3 = new UserResponse(4L, "Alex", "Georgescu", "alex.georgescu@gmail.com", 31, LocalDate.now(), "0743987654", "alexPass");
        UserResponse userResponse4 = new UserResponse(5L, "Elena", "Dumitrescu", "elena.dumitrescu@gmail.com", 24, LocalDate.now(), "0755123987", "elenaPwd");
        UserResponse userResponse5 = new UserResponse(6L, "Radu", "Stan", "radu.stan@gmail.com", 29, LocalDate.now(), "0766234567", "raduPass");

//        List<User> users = List.of(user1,user2,user3,user4,user5);
//        Page<UserResponse> expected = new PageImpl<>(List.of(userResponse1));
//        when(userRepository.searchByFirstNameOrEmailOrPhoneNumber("Andrei", Pageable.unpaged())).thenReturn(expected);
//
//        Page<UserResponse> actual = userQueryService.searchByFirstNameOrEmailOrPhoneNumber("Andrei",Pageable.unpaged());
//
//        assertEquals(expected,actual);
        List<User> users = List.of(user1, user2, user3, user4, user5);
        Pageable pageable = Pageable.unpaged();

        when(userRepository.searchByFirstNameOrEmailOrPhoneNumber("a", pageable))
                .thenReturn(new PageImpl<>(users));

        Page<UserResponse> actual =
                userQueryService.searchByFirstNameOrEmailOrPhoneNumber("a", pageable);

        assertEquals(5, actual.getTotalElements());
        assertEquals(
                List.of(userResponse1, userResponse2, userResponse3, userResponse4, userResponse5),
                actual.getContent()
        );


    }

}
