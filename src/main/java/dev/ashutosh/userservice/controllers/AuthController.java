package dev.ashutosh.userservice.controllers;

import dev.ashutosh.userservice.dtos.*;
import dev.ashutosh.userservice.models.SessionStatus;
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
                System.out.println("Inside login");
                return authService.login(request.getEmail(), request.getPassword());


        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request){

                authService.logout(request.getToken(), request.getUserId());
                return new ResponseEntity<>(HttpStatus.OK);

        }
        @PostMapping("/signup")
        public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request){

                UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());
                //System.out.println(request.getEmail());
                return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
        @PostMapping("/validate")
        public SessionStatus validateToken(@RequestBody ValidateTokenRequestDto request){

                return authService.validateToken(request.getUserId(), request.getToken());
        }
}
