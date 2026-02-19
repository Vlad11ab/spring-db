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

    @Column
    @Email
    @NotBlank(message = "Email obligatoriu")
    private String email;

    @Positive(message = "Varsta > 16 ani")
    private int age;

    @NotNull(message = "Data angajarii obligatorie")
    private LocalDate hireDate;

    @Size(min = 5, max = 20, message = "Parola trebuie sa contina intre 5-20 de caractere")
    @NotBlank
    private String password;

    @NotBlank(message = "Numar de telefon obligatoriu")
    private String phoneNumber;
}
