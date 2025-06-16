package com.uam.agendave.dto.Actividad;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TipoActividadDTO {

    private UUID id;
    private String nombreTipo;
    private String facultadEncargada;
}
