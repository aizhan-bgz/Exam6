package org.itacademy.secutityjvt;

import org.itacademy.secutityjvt.entity.Role;
import org.itacademy.secutityjvt.entity.User;
import org.itacademy.secutityjvt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class SecutityJvtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecutityJvtApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return arg -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));


            userService.saveUser(new User(null, "Айжан", "admin", "1111", "ACTIVE",new ArrayList<>()));
            userService.saveUser(new User(null, "Ширин", "user1", "2222", "ACTIVE",new ArrayList<>()));
            userService.saveUser(new User(null, "Мээрим", "user2", "3333", "ACTIVE",new ArrayList<>()));

            userService.addRoleToUser("admin", "ROLE_SUPER_ADMIN");
            userService.addRoleToUser("user1", "ROLE_USER");
            userService.addRoleToUser("user2", "ROLE_USER");

        };
    }


}
