package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String cif;
    private String password;
}

