package com.example.authservice1.Controller;

import com.example.authservice1.Config.JwtTokenUtil;
import com.example.authservice1.Entity.JwtRequest;
import com.example.authservice1.Entity.JwtResponse;
import com.example.authservice1.Entity.Role;
import com.example.authservice1.Entity.User;
import com.example.authservice1.Service.JwtUserDetailsService;
import com.example.authservice1.Service.UserService;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
//    private final RedisTemplate<Object, Object> template;
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<String, Object> myMap = hazelcastInstance.getMap("auth-map");

    public static final long JWT_TOKEN_VALIDITY = 15 * 60;
    @GetMapping("/getUser/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok().body(userService.getUserByUserName(username));
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getListUser(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addAnewUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.saveUser(user));
    }
    @PostMapping("/addRole")
    public ResponseEntity<Role> addAnewRole(@RequestBody Role role){
        return ResponseEntity.ok().body(userService.saveRole(role));
    }

    @PostMapping("/addRoleToUser")
    public void addAnewRole(@RequestParam String username, @RequestParam String roleName){
        userService.addRoleToUser(username, roleName);
    }

//    @PostMapping("/validate")
//    public String validate(@RequestBody String Token,  loginRequest){
//
//        return null;
//
//    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        myMap.put(token, userDetails.getUsername(), JWT_TOKEN_VALIDITY * 1000, TimeUnit.MILLISECONDS);
//        template.opsForValue().set(token, userDetails.getUsername());
//        template.expire(token, JWT_TOKEN_VALIDITY * 1000, TimeUnit.MILLISECONDS);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


    @Data
    private static class LoginRequest{
        String username;
        String password;
    }
}
