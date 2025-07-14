package com.uam.agendave.dto.EntidadesConfig;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ZonaAsistenciaDTO {
    @NotNull
    private UUID idActividad;

    private Double lat;
    private Double lng;

    @DecimalMin(value= "10", message = "El Radio debe de ser de al menos 10 metros.")
    private Double radioMetros;

    @NotNull(message = "Debe especificar el tiempo límite para marcar asistencia.")
    @Future(message = "El tiempo límite debe ser una fecha futura.")
    private LocalDateTime tiempoLimite;

    public ZonaAsistenciaDTO(UUID idActividad, Double lat, Double lng, Double radioMetros, LocalDateTime tiempoLimite) {
        this.idActividad = idActividad;
        this.lat = lat;
        this.lng = lng;
        this.radioMetros = radioMetros;
        this.tiempoLimite = tiempoLimite;
    }
}

