package com.bookshelf.idp.authservice.service;

import com.bookshelf.idp.authservice.client.DatabaseServiceClient;
import com.bookshelf.idp.authservice.dto.UserResponseDto;
import com.bookshelf.idp.authservice.exception.NotFoundException;
import com.bookshelf.idp.authservice.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final DatabaseServiceClient dbClient;

    public UserService(DatabaseServiceClient dbClient) {
        this.dbClient = dbClient;
    }

    public UserResponseDto getMe(String email) {
        UserModel user = dbClient.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toDto(user);
    }

    public List<UserResponseDto> getAll() {
        return dbClient.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(UUID id) {
        dbClient.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        dbClient.deleteById(id);
    }

    private UserResponseDto toDto(UserModel user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}