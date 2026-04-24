package com.bookshelf.idp.authservice.service;

import com.bookshelf.idp.authservice.config.security.JwtGenerator;
import com.bookshelf.idp.authservice.dto.*;
import com.bookshelf.idp.authservice.entity.User;
import com.bookshelf.idp.authservice.entity.UserRole;
import com.bookshelf.idp.authservice.exception.BadRequestException;
import com.bookshelf.idp.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    public void register(RegisterRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email is already used");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        if (!userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Wrong credentials");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(dto.getEmail()).get();
        String token = jwtGenerator.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponseDto(token, "Bearer", user.getName(), user.getRole().name());
    }
}