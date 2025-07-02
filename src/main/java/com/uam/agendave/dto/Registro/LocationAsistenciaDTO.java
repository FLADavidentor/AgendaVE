package com.uam.agendave.dto.Registro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LocationAsistenciaDTO {
    @NotBlank(message = "El CIF del estudiante es obligatorio")
    private String cif;
    @NotNull(message = "El id de Actividad es obligatorio")
    private UUID idActividad;
    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;
    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;
}
