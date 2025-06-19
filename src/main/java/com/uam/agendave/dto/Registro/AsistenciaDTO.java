package com.uam.agendave.dto.Registro;

import com.uam.agendave.model.EstadoAsistencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AsistenciaDTO {

    @NotBlank(message = "El CIF del estudiante es obligatorio")
    private String cif;
    @NotNull(message = "El id de Actividad es obligatorio")
    private UUID idActividad;
    @NotNull(message = "Debe de incluirse el Estado de Asistencia")
    private EstadoAsistencia estadoAsistencia;
}
