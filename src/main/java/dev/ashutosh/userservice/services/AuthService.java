package dev.ashutosh.userservice.services;

import dev.ashutosh.userservice.dtos.UserDto;
import dev.ashutosh.userservice.models.Session;
import dev.ashutosh.userservice.models.SessionStatus;
import dev.ashutosh.userservice.models.User;
import dev.ashutosh.userservice.repositories.RoleRepository;
import dev.ashutosh.userservice.repositories.SessionRepository;
import dev.ashutosh.userservice.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private SessionRepository sessionRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public AuthService(UserRepository userRepository, SessionRepository sessionRepository){
//    BCryptPasswordEncoder bCryptPasswordEncoder)
//       this.bCryptPasswordEncoder = bCryptPasswordEncoder;
         this.userRepository = userRepository;
         this.sessionRepository = sessionRepository;
    }
//    private static String randomString(int length) {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (int i = 0; i < length; i++) {
//            switch (new Random().nextInt(3)) {
//                case 0:
//                    stringBuilder.append((char) (new Random().nextInt(9) + 48));
//                    break;
//                case 1:
//                    stringBuilder.append((char) (new Random().nextInt(25) + 65));
//                    break;
//                case 2:
//                    stringBuilder.append((char) (new Random().nextInt(25) + 97));
//                    break;
//                default:
//                    break;
//            }
//        }
//        return stringBuilder.toString();
//    }
    public UserDto signUp(String email, String password){
        System.out.println("Inside signup");
       User user = new User();
       user.setEmail(email);
//       user.setPassword(bCryptPasswordEncoder.encode(password));
       User saveduser = userRepository.save(user);
       return UserDto.from(saveduser);

   }
    public ResponseEntity<UserDto> login(String email, String password){
         Optional<User> userOptional = userRepository.findByEmail(email);
         if(userOptional.isEmpty()){
             return null;

         }
         User user = userOptional.get();
//         if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
//                throw new RuntimeException("Invalid Password");
//         }
        //String token = randomString(30);
          MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS256
        key = alg.key().build();
        //SignatureAlgorithm alg = SignatureAlgorithm.HS256;
        //key = Keys.secretKeyFor(alg);

        Map<String, Object> jsonForJwt = new HashMap<>();
        jsonForJwt.put("email", user.getEmail());
        jsonForJwt.put("createdAt", new Date());
        jsonForJwt.put("expiryAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));
        //jsonForJwt.put("role", user.getRole().getName());

        String token = Jwts.builder()
                .claims(jsonForJwt)
                .signWith(key, alg)
                .compact();

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

    public ResponseEntity<Void> logout(String token, Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()){
            throw new RuntimeException("Invalid Token");
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public SessionStatus validateToken(Long userId, String token){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()){
            throw new RuntimeException("Session Expired");
        }
        Session session = sessionOptional.get();
        if(session.getSessionStatus() == SessionStatus.ENDED){
            throw new RuntimeException("Session Expired");
        }
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(key)  // Replace with the actual signing key
                .build().parseSignedClaims(token);
//        Jws<Claims> claims = Jwts.parser()
//                .build()
//                .parseSignedClaims(token);
        String email = (String)claims.getPayload().get("email");
        Date createdAt = (Date)claims.getPayload().get("createdAt");
        Date expiryAt = (Date)claims.getPayload().get("expiryAt");

        if (new Date().after(expiryAt)) {
            return SessionStatus.ENDED;
        }
//        if (createdAt.before(new Date())) {
//            return SessionStatus.ENDED;
//        }
        return SessionStatus.ACTIVE;
    }
}
