package com.uam.agendave.dto.Actividad;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LugarDTO {

    private UUID id;
    private String nombre;
    private int capacidad;
    private Double latitud;
    private Double longitud;
}
