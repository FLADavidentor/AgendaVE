package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NombreActividadDTO {

    private UUID id;
    private String nombre;
    private UUID idTipoActividad;
}
