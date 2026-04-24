package com.bookshelf.idp.authservice.dto;


import lombok.*;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String role;
}
