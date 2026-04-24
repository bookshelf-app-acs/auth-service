package com.bookshelf.idp.authservice.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LoginRequestDto {
    private String email;
    private String password;
}
