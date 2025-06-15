package com.uam.agendave.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "El CIF es obligatorio")
    private String cif;
    @NotBlank(message = "La contrasena es obligatoria")
    private String password;
}

