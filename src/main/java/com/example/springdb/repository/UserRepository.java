package com.example.springdb.repository;

import com.example.springdb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> , JpaSpecificationExecutor<User> {

    Optional<User> findUserByEmail(String email);

    @Query("select u from User u where lower(u.firstName) = lower(:firstName)")
    Optional<User> findByFirstNameIgnoreCaseJPQL(@Param("firstName")String firstName);

    @Query("select u from User u where lower(u.lastName) = lower(:lastName)")
    Optional<User> findByLastNameIgnoreCaseJPQL(@Param("lastName") String lastName);

    @Query("select u from User u where lower(u.email) = lower(:email)")
    Optional<User> findByEmailIgnoreCaseJPQL(@Param("email") String email);

    @Query("select u from User u\n"+
            "where u.age between :minAge and :maxAge\n" +
            "order by u.age asc")
    List<User> findByAgeRange (@Param("minAge") int minAge,
                               @Param("maxAge") int maxAge);

    @Query("select u from User u\n" +
            "where u.hireDate >= :from and u.hireDate <= :to\n" +
            "order by u.hireDate asc")
    List<User> findHiredBetween(@Param("from") LocalDate from,
                                @Param("to") LocalDate to);

    @Query("select u from User u\n" +
            "where lower(u.firstName) like lower(concat('%', :q, '%'))" +
            "or lower(u.email) like lower(concat('%', :q, '%'))" +
            "or u.phoneNumber like concat('%', :q, '%')")
    Page<User> searchByFirstNameOrEmailOrPhoneNumber(@Param("q") String q, Pageable pageable);


    @Query("select u from User u where" +
            "(u.firstName is null or u.firstName=:firstName) and" +
            "(u.lastName is null or u.lastName=:lastName) and " +
            "(u.email is null or u.email=:email) and" +
            "(u.age is NULL or u.age=:age) and" +
            "(u.hireDate is null or u.hireDate=:hireDate) and " +
            "(u.password is null or u.password=:password) and" +
            "(u.phoneNumber is null or u.phoneNumber=:phoneNumber)")
    List<User> search(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("age") int age,
            @Param("hireDate") LocalDate hireDate,
            @Param("password") String password,
            @Param("phoneNumber") String phoneNumber);

    @Query("select count(u) from User u where u.hireDate <= :date")
    long countHiredBefore(@Param("date") LocalDate date);

    @Query("select (count(u) > 0) from User u where lower(u.email) = :email")
    boolean existsByEmailJPQL(@Param("email") String email);
}

