package com.uam.agendave.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AsistenciaDTO {

    private String cif;
    private UUID idActividad;
}
