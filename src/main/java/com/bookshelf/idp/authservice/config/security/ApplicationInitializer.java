package com.bookshelf.idp.authservice.config.security;

import com.bookshelf.idp.authservice.client.DatabaseServiceClient;
import com.bookshelf.idp.authservice.model.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitializer implements ApplicationRunner {

    private final DatabaseServiceClient dbClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public ApplicationInitializer(DatabaseServiceClient dbClient, PasswordEncoder passwordEncoder) {
        this.dbClient = dbClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            ensureUser(adminName, adminEmail, adminPassword, "ADMIN");
            ensureUser("Ada Cosma", "ada@bookshelf.ro", "parola123", "USER");
            ensureUser("Teodora Popescu", "teodora@bookshelf.ro", "parola123", "USER");
            ensureUser("Maria Ionescu", "maria@bookshelf.ro", "parola123", "USER");
            ensureUser("Andrei Stan", "andrei@bookshelf.ro", "parola123", "USER");
            ensureUser("Cristian Marin", "cristian@bookshelf.ro", "parola123", "USER");
        } catch (Exception e) {
            System.out.println("Database service not available, skipping initialization: " + e.getMessage());
        }
    }

    private void ensureUser(String name, String email, String rawPassword, String role) {
        if (!dbClient.existsByEmail(email)) {
            UserModel user = new UserModel();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            dbClient.save(user);
        }
    }
}
