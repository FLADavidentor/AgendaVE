package com.uam.agendave.dto.Actividad;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NombreActividadDTO {

    private UUID id;
    private String nombre;
}
