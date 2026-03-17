package com.example.springdb.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "Prenume obligatoriu")
    @Size(min = 3, max = 50, message = "Prenumele trebuie sa fie cuprins intre 3-50")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Nume de familie obligatoriu")
    @Size(min = 3, max = 50, message = "Numele de familie trebuie sa fie cuprins intre 3-50")
    private String lastName;

    @Column(name = "email")
    @Email
    @NotBlank(message = "Email obligatoriu")
    private String email;

    @Column(name = "age")
    @Positive(message = "Varsta > 16 ani")
    private Integer age;

    @Column(name = "hire_date")
    @NotNull(message = "Data angajarii obligatorie")
    private LocalDate hireDate;

    @Column(name = "password")
    @Size(min = 5, max = 20, message = "Parola trebuie sa contina intre 5-20 de caractere")
    @NotBlank
    private String password;

    @Column(name = "phone_number")
    @NotBlank(message = "Numar de telefon obligatoriu")
    private String phoneNumber;

    public User(String firstName, String lastName, String email, Integer age, LocalDate hireDate, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.hireDate = hireDate;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
