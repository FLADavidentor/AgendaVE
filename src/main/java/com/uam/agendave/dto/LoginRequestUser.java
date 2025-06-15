package com.uam.agendave.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestUser {
    @NotBlank(message = "El usuario es obligatorio")
    private String username;
    @NotBlank(message = "La contrasena es obligatoria")
    private String password;
}