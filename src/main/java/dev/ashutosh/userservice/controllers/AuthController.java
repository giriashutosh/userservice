package dev.ashutosh.userservice.controllers;

import dev.ashutosh.userservice.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
        private AuthService authService;
        public AuthController(AuthService authService){
                this.authService = authService;

        }

        public void login(){

        }
        public void logout(){

        }
        @PostMapping("/signup")
        public void signup(){

        }
}
