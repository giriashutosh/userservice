package dev.ashutosh.userservice.controllers;

import org.apache.catalina.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserController(){

    }

    @PostMapping("/login")
    public void login(){

    }
    @PostMapping("/logout")
    public void logout(){

    }
    @PostMapping
    public void signup(){

    }
}
