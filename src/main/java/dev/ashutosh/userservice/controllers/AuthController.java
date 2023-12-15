package dev.ashutosh.userservice.controllers;

import dev.ashutosh.userservice.dtos.LoginRequestDto;
import dev.ashutosh.userservice.dtos.SignUpRequestDto;
import dev.ashutosh.userservice.dtos.UserDto;
import dev.ashutosh.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
        private AuthService authService;
        public AuthController(AuthService authService){
                this.authService = authService;

        }

        @PostMapping("/login")
        public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request){
                return authService.login(request.getEmail(), request.getPassword());


        }
        public void logout(){

        }
        @PostMapping("/signup")
        public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request){
                System.out.println("Inside signup");
                UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());
                //System.out.println(request.getEmail());
                return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
}
