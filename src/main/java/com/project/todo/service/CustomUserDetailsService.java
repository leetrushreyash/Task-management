package com.project.todo.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.todo.entity.UserEntry;
import com.project.todo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntry userEntry = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("user not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntry.getEmail())
                .password(userEntry.getPassword())
                .roles(userEntry.getRole().name())
                .build();

    }
}
