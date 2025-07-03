package com.uam.agendave.dto.Actividad;

import com.uam.agendave.model.TipoConvalidacion;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.sql.Time;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ActividadDTO {

    private UUID id;
    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;
    @NotNull(message = "La Fecha es obligatoria")
    private Date fecha;
    @NotNull(message = "La Hora de inicio es obligatoria")
    private Time horaInicio;
    @NotNull(message= "La Hora de fin es obligatoria")
    private Time horaFin;
    private boolean estado;
    @Min(value = 1, message = "El cupo debe ser al menos de 1")
    private int cupo;

    @NotBlank(message = "El nombre de la actividad es obligatorio")
    private String nombreActividad;
    @NotBlank(message = "El lugar es obligatorio")
    private String lugar;

    private Integer cuposRestantes;

    private UUID idTransporte;

    private Map<TipoConvalidacion, Integer> convalidacionesPermitidas; // Convalidaciones por tipo
    private Integer totalConvalidacionesPermitidas;

    private ImagenDTO imagen;
    private String imagenPath;
}
