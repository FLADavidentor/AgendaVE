package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsuarioDTO {

    private UUID id;
    private String username;
    private String correo;
    private String nombre;
    private String apellido;
}
