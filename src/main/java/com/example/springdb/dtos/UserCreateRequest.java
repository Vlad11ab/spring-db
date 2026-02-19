package com.example.springdb.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserCreateRequest(
        @NotBlank(message = "Prenumele e obligatoriu")
        @Size(min = 3, max = 12, message = "Nume intre 3-12 caractere")
        String firstName,

        @NotBlank(message = "Numele de familie e obligatoriu")
        @Size(min = 3, max = 12, message = "Nume intre 3-12 caractere")
        String lastName,

        @NotBlank(message = "Email obligatoriu")
        @Email(message ="Format invalid")
        String email,

        @NotNull(message = "Varsta obligatorie")
        @Positive(message = "Varsta trebuie sa fie >0")
        int age,

        @NotNull
        @PastOrPresent(message = "Data angajarii nu poate fi in viitor")
        LocalDate hireDate,

        @NotBlank(message = "Numar de telefon obligatoriu")
        @Pattern(
                regexp = "^(\\+\\d{1,3})?\\s?\\d{9,15}$",
                message = "Telefon invalid (ex: 07xxxxxxxx sau +40xxxxxxxxx)"
        )
        String phoneNumber,

        @Size(min = 5, max = 20, message = "Parola trebuie sa continta intre 5-20 de caractere")
        @NotBlank
        String password

) {}
