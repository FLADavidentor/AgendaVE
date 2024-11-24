package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegistroDTO {

    private UUID id;
    private boolean convalidacion;
    private boolean transporte;

    private UUID idEstudiante;
    private UUID idActividad;
}
