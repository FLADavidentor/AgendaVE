package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DetalleAsistenciaDTO {

    private UUID id;
    private String estadoAsistencia; // ENUM representado como String
    private UUID idAsistencia;
    private UUID idRegistro;
}
