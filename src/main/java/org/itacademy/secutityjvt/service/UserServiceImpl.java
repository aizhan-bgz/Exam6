package org.itacademy.secutityjvt.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itacademy.secutityjvt.entity.Role;
import org.itacademy.secutityjvt.entity.User;
import org.itacademy.secutityjvt.repo.RoleRepo;
import org.itacademy.secutityjvt.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("ACTIVE");
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }


    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }


    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public void blockedUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user.getStatus().equals("DELETED")) throw new EntityNotFoundException("Пользователь не найден");
        if (user.getStatus().equals("BLOCKED")) throw new RuntimeException("Пользователь уже заблокирован");
        user.setStatus("BLOCKED");
        userRepo.save(user);
    }

    @Override
    public void unlockedUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user.getStatus().equals("DELETED")) throw new EntityNotFoundException("Пользователь не найден");
        if (user.getStatus().equals("ACTIVE")) throw new RuntimeException("Пользователь не заблокирован");
        user.setStatus("ACTIVE");
        userRepo.save(user);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user.getStatus().equals("DELETED")) throw new EntityNotFoundException("Пользователь не найден");
        user.setStatus("DELETED");
        userRepo.save(user);
    }


}