package ru.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private String username;
    private String password;
    private Role role;

    public enum Role {
        ADMIN, USER;
    }
}
