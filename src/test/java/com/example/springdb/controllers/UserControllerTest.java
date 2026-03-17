package com.example.springdb.controllers;


import com.example.springdb.controller.UserController;
import com.example.springdb.service.command.UserCommandService;
import com.example.springdb.service.query.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.*;


@WebMvcTest(controller =)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserQueryService userQueryService;

    @MockBean
    private UserCommandService userCommandService;
}
