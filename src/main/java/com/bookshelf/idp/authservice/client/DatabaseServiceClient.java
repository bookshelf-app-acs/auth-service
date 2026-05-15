package com.bookshelf.idp.authservice.client;

import com.bookshelf.idp.authservice.model.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DatabaseServiceClient {

    private final RestTemplate restTemplate;

    @Value("${database.service.url}")
    private String databaseServiceUrl;

    public DatabaseServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<UserModel> findByEmail(String email) {
        try {
            UserModel user = restTemplate.getForObject(
                    databaseServiceUrl + "/internal/users/email/" + email, UserModel.class);
            return Optional.ofNullable(user);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public Boolean existsByEmail(String email) {
        return restTemplate.getForObject(
                databaseServiceUrl + "/internal/users/exists/email/" + email, Boolean.class);
    }

    public UserModel save(UserModel user) {
        return restTemplate.postForObject(
                databaseServiceUrl + "/internal/users", user, UserModel.class);
    }

    public List<UserModel> findAll() {
        UserModel[] users = restTemplate.getForObject(
                databaseServiceUrl + "/internal/users", UserModel[].class);
        return users != null ? Arrays.asList(users) : List.of();
    }

    public Optional<UserModel> findById(UUID id) {
        try {
            UserModel user = restTemplate.getForObject(
                    databaseServiceUrl + "/internal/users/" + id, UserModel.class);
            return Optional.ofNullable(user);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public void deleteById(UUID id) {
        restTemplate.delete(databaseServiceUrl + "/internal/users/" + id);
    }
}