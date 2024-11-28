package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestUser {
    private String username;
    private String password;
}