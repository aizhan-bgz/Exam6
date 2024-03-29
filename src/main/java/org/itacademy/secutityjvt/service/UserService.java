package org.itacademy.secutityjvt.service;

import org.itacademy.secutityjvt.entity.Role;
import org.itacademy.secutityjvt.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);


    User getUser(String username);

    List<User> getUsers();

    void blockedUser(String username);
    void unlockedUser(String username);

    void deleteUser(String username);



}
