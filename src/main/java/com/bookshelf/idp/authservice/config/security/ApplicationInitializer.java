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
            if (!dbClient.existsByEmail(adminEmail)) {
                UserModel admin = new UserModel();
                admin.setName(adminName);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole("ADMIN");
                dbClient.save(admin);
            }

            if (!dbClient.existsByEmail("ada@bookshelf.ro")) {
                UserModel user = new UserModel();
                user.setName("Ada");
                user.setEmail("ada@bookshelf.ro");
                user.setPassword(passwordEncoder.encode("parola123"));
                user.setRole("USER");
                dbClient.save(user);
            }
        } catch (Exception e) {
            System.out.println("Database service not available, skipping initialization: " + e.getMessage());
        }
    }
}