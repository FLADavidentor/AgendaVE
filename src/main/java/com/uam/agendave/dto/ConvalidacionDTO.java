package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ConvalidacionDTO {
    private UUID id;
    private String idEstudiante;
    private UUID idActividad;
    private int creditosConvalidados;
}
