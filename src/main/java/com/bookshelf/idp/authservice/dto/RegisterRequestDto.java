package com.bookshelf.idp.authservice.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegisterRequestDto {
    private String name;
    private String email;
    private String password;
}
