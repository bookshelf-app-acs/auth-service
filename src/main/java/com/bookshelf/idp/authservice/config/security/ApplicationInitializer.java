package com.bookshelf.idp.authservice.config.security;

import com.bookshelf.idp.authservice.entity.User;
import com.bookshelf.idp.authservice.entity.UserRole;
import com.bookshelf.idp.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public ApplicationInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin created: " + adminEmail);
        }

        if (!userRepository.existsByEmail("ada@bookshelf.ro")) {
            User user = new User();
            user.setName("Ada");
            user.setEmail("ada@bookshelf.ro");
            user.setPassword(passwordEncoder.encode("parola123"));
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }
    }
}