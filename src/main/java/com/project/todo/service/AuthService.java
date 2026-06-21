package com.project.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.todo.dto.AuthResponse;
import com.project.todo.dto.UserLoginRequest;
import com.project.todo.dto.UserRegisterRequest;
import com.project.todo.entity.UserEntry;
import com.project.todo.enums.RoleEnum;
import com.project.todo.exception.EmailExistsException;
import com.project.todo.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email()))
            throw new EmailExistsException("email already exists");
        UserEntry userEntry = new UserEntry();
        userEntry.setEmail(request.email());
        userEntry.setRole(RoleEnum.USER);
        userEntry.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(userEntry);
        String jwtToken = jwtService.generateToken(userEntry.getEmail(), userEntry.getRole());
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserEntry user = userRepository.findByEmail(request.email())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(jwtToken);
    }

}
