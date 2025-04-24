package com.uam.agendave.dto;

import com.uam.agendave.model.EstadoAsistencia;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AsistenciaDTO {

    private String cif;
    private UUID idActividad;
    private EstadoAsistencia estadoAsistencia;
}
