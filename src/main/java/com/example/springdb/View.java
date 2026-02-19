package com.example.springdb;


import com.example.springdb.dtos.UserCreateRequest;
import com.example.springdb.dtos.UserPatchRequest;
import com.example.springdb.dtos.UserResponse;
import com.example.springdb.exceptions.EmailAlreadyExistsException;
import com.example.springdb.exceptions.UserNotFoundException;
import com.example.springdb.mappers.UserMapper;
import com.example.springdb.service.command.UserCommandService;
import com.example.springdb.service.query.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class View {

    UserCommandService userCommandService;
    UserQueryService userQueryService;

    UserMapper userMapper;

    Scanner scanner;


    public View(UserCommandService userCommandService, UserQueryService userQueryService, UserMapper userMapper, Scanner scanner){
        this.userMapper = userMapper;
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.scanner = scanner;
        this.play();
    }

    public void menu(){
        System.out.println("1->Add user");
        System.out.println("2->Update user");
        System.out.println("3->Delete user");
        System.out.println("4->Show users");
        System.out.println("5->Find user by email");
        System.out.println("6->Find user by last name");
        System.out.println("7->Find user by emailIgnoreCase");
        System.out.println("8->Find users between an age range");
        System.out.println("9->Find users hired between a date range");
        System.out.println("10->Display the number of people hired before a certain date");
        System.out.println("11->Find if there there exists a user with a certain email");
        System.out.println("12->Search");
    }

    public void play(){
        boolean running = true;

        while(running){
            menu();
            int choose = Integer.parseInt(scanner.nextLine());

            switch(choose){
                case 1:
                    select1();
                    break;
                case 2:
                    select2();
                    break;
                case 3:
                    select3();
                    break;
                case 4:
                    select4();
                    break;
                case 5:
                    select5();
                    break;
                case 6:
                    select6();
                    break;
                case 7:
                    select7();
                    break;
                case 8:
                    select8();
                    break;
                case 9:
                    select9();
                    break;
                case 10:
                    select10();
                    break;
                case 11:
                    select11();
                    break;
                case 12:
                    select12();
                    break;
            }
        }
    }




    private void select1(){

        System.out.println("firstName:");
        String firstName = scanner.nextLine();
        System.out.println("lastName:");
        String lastName = scanner.nextLine();
        System.out.println("email:");
        String email = scanner.nextLine();
        System.out.println("age:");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.println("hireDate:");
        LocalDate hireDate = LocalDate.parse(scanner.nextLine());
        System.out.println("phone:");
        String phone = scanner.nextLine();
        System.out.println("password:");
        String password = scanner.nextLine();

        UserCreateRequest request = new UserCreateRequest(firstName,lastName,email,age,hireDate,phone,password);
        try{
            UserResponse response = userCommandService.create(request);
            System.out.println("Utilizator creat: " + response);
        } catch(EmailAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

    }

    private void select2(){

        System.out.println("UserId:");
        Long userId = Long.parseLong(scanner.nextLine());
        System.out.println("NewAge:");
        int newAge = Integer.parseInt(scanner.nextLine());
        System.out.println("NewEmail:");
        String newEmail = scanner.nextLine();
        System.out.println("newPassword:");
        String newPassword = scanner.nextLine();

        UserPatchRequest request = new UserPatchRequest(newAge,newEmail,newPassword);
        try{
            UserResponse response = userCommandService.patch(userId,request);
            System.out.println("Utilizator actualizat: " + response);
        } catch( UserNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    private void select3(){
        System.out.println("Id-ul utilizatorului de sters:");
        Long userId = Long.parseLong(scanner.nextLine());

        try{
            userCommandService.delete(userId);
            System.out.println("Utilizator sters cu succes");
        } catch( UserNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private void select4(){
        System.out.println("Lista useri:");

        List<UserResponse> users = userQueryService.findAllUsers();

        if(users.isEmpty()){
            System.out.println("Lista este goala");
        }else{
            users.forEach(System.out::println);
        }
    }

    private void select5(){
        System.out.println("Introdu email:");
        String email = scanner.nextLine();

        Optional<UserResponse> userOptional = userQueryService.findByEmail(email);


        if (userOptional.isPresent()) {
                System.out.println("User gasit " + userOptional.get());
        } else {
                System.out.println("Niciun user cu acest email nu a fost gasit");
        }

    }

    private void select6(){
        System.out.println("Last Name:");
        String lastName = scanner.nextLine();

        Optional<UserResponse> userOptional = userQueryService.findByLastName(lastName);

        if(userOptional.isPresent()){
            System.out.println("User gasit " + userOptional.get());
        }else{
            System.out.println("Niciun user cu acest nume de familie nu a fost gasit");
        }
    }

    private void select7(){
        System.out.println("Introdu email:");
        String email = scanner.nextLine();

        Optional<UserResponse> userOptional = userQueryService.findByEmailIgnoreCase(email);


        if (userOptional.isPresent()) {
            System.out.println("User gasit " + userOptional.get());
        } else {
            System.out.println("Niciun user cu acest email nu a fost gasit");
        }

    }

    private void select8(){
        System.out.println("Introdu varsta minima");
        int minAge = Integer.parseInt(scanner.nextLine());
        System.out.println("Introdu varsta maxima");
        int maxAge = Integer.parseInt(scanner.nextLine());

        List<UserResponse> users = userQueryService.findByAgeRange(minAge,maxAge);

        if(users.isEmpty()){
            System.out.println("Niciun user gasit in intervalul de varsta dorit");
        }else{
            users.forEach(System.out::println);
        }
    }

    private void select9(){
        System.out.println("Introdu data de start YYYY-MM-DD");
        String fromString = scanner.nextLine();
        LocalDate from = LocalDate.parse(fromString);
        System.out.println("Introdu data de sfarsit YYYY-MM-DD");
        String toString = scanner.nextLine();
        LocalDate to = LocalDate.parse(toString);

        List<UserResponse> users = userQueryService.findHiredBetween(from, to);

        if(users.isEmpty()){
            System.out.println("Nu exista");
        }else{
            users.forEach(System.out::println);
        }

    }

    private void select10(){
        System.out.println("Introdu o data(YYYY-MM-DD): ");
        String dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString);

        long usersHiredBefore = userQueryService.countHiredBefore(date);

        if(usersHiredBefore == 0){
            System.out.println("Nimeni nu a fost angajat inainte de data introdusa");
        }else{
            System.out.println(usersHiredBefore + " au fost angajati inainte de date " + date);
        }
    }

    private void select11(){
        System.out.println("Introdu un email: ");
        String email = scanner.nextLine();

        boolean userOptional = userQueryService.userExistsByEmail(email);

        if(userOptional){
            System.out.println("Exista!");
        }else{
            System.out.println("Nu exista");
        }
    }

    private void select12(){
        System.out.println("Search:");
        String input = scanner.nextLine();

        Page<UserResponse> search = userQueryService.searchByFirstNameOrEmailOrPhoneNumber(input, Pageable.ofSize(10));

        if(search.isEmpty()){
            System.out.println("Niciun rezultat nu a fost gasit");
        }else{
            search.forEach(System.out::println);
        }
    }





}
