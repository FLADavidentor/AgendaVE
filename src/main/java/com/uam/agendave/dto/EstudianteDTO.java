package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EstudianteDTO {

    private UUID id;
    private String nombre;
    private String nombre2;
    private String apellido;
    private String apellido2;
    private String correo;
    private String numeroTelefono;
    private String cif;
}
