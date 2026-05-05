package com.example.springdb.model;


import com.example.springdb.config.security.UserPermissions;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class UserApp implements UserDetails {
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

    public UserApp(String firstName, String lastName, String email, Integer age, LocalDate hireDate, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.hireDate = hireDate;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name =  "user_id"))
    @Column(name = "permission", nullable = false)
    @Enumerated(EnumType.STRING)
    public Set<UserPermissions> permissions = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(UserPermissions::getPermission)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
