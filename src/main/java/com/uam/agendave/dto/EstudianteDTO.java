package com.uam.agendave.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class EstudianteDTO {

    private String cif;
    private String correo;
    private String nombre;
    private String apellido;
    private String facultad;
    private String carrera;


}
