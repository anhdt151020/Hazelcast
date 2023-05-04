package com.example.authservice1.Service;

import com.example.authservice1.Entity.Role;
import com.example.authservice1.Entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUserByUserName(String username);

    List<User> getUsers();
}
