package com.bookshelf.idp.authservice.model;

import lombok.*;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserModel {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
}