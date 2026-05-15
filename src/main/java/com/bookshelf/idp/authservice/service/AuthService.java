package com.bookshelf.idp.authservice.service;

import com.bookshelf.idp.authservice.client.DatabaseServiceClient;
import com.bookshelf.idp.authservice.config.security.JwtGenerator;
import com.bookshelf.idp.authservice.dto.*;
import com.bookshelf.idp.authservice.exception.BadRequestException;
import com.bookshelf.idp.authservice.model.UserModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DatabaseServiceClient dbClient;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    public AuthService(DatabaseServiceClient dbClient, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
        this.dbClient = dbClient;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    public void register(RegisterRequestDto dto) {
        if (dbClient.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email is already used");
        }
        UserModel user = new UserModel();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");
        dbClient.save(user);
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        if (!dbClient.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Wrong credentials");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserModel user = dbClient.findByEmail(dto.getEmail()).get();
        String token = jwtGenerator.generateToken(user.getEmail(), user.getRole());
        return new LoginResponseDto(token, "Bearer", user.getName(), user.getRole());
    }
}