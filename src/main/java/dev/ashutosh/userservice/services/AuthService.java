package dev.ashutosh.userservice.services;

import dev.ashutosh.userservice.dtos.UserDto;
import dev.ashutosh.userservice.models.Session;
import dev.ashutosh.userservice.models.SessionStatus;
import dev.ashutosh.userservice.models.User;
import dev.ashutosh.userservice.repositories.RoleRepository;
import dev.ashutosh.userservice.repositories.SessionRepository;
import dev.ashutosh.userservice.repositories.UserRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
       this.bCryptPasswordEncoder = bCryptPasswordEncoder;
         this.userRepository = userRepository;
    }
    private static String randomString(int length) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            switch (new Random().nextInt(3)) {
                case 0:
                    stringBuilder.append((char) (new Random().nextInt(9) + 48));
                    break;
                case 1:
                    stringBuilder.append((char) (new Random().nextInt(25) + 65));
                    break;
                case 2:
                    stringBuilder.append((char) (new Random().nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return stringBuilder.toString();
    }
    public UserDto signUp(String email, String password){
        System.out.println("Inside signup");
       User user = new User();
       user.setEmail(email);
       user.setPassword(bCryptPasswordEncoder.encode(password));
       User saveduser = userRepository.save(user);
       return UserDto.from(saveduser);

   }
    public ResponseEntity<UserDto> login(String email, String password){
         Optional<User> userOptional = userRepository.findByEmail(email);
         if(userOptional.isEmpty()){
             return null;

         }
         User user = userOptional.get();
         if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
                throw new RuntimeException("Invalid Password");
         }
        String token = randomString(30);
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = UserDto.from(user);
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);
        return response;
    }
}
