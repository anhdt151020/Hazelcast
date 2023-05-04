package com.example.authservice1;

import com.example.authservice1.Entity.Role;
import com.example.authservice1.Entity.User;
import com.example.authservice1.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
//@EnableEurekaClient
public class AuthService1Application {

    public static void main(String[] args) {
        SpringApplication.run(AuthService1Application.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(1L, "DinhTuanAnh", "tuanAnh", "1234", new ArrayList<>()));

            userService.addRoleToUser("tuanAnh", "ROLE_USER");
            userService.addRoleToUser("tuanAnh", "ROLE_MANAGER");
            userService.addRoleToUser("tuanAnh", "ROLE_ADMIN");
            userService.addRoleToUser("tuanAnh", "ROLE_SUPER_ADMIN");

        };
    }

}
