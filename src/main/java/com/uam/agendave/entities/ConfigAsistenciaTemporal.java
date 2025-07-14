package com.uam.agendave.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ConfigAsistenciaTemporal {
    private Double radioMetros;
    private LocalDateTime tiempoLimite;
    private Double lat;
    private Double lng;


    public ConfigAsistenciaTemporal(Double radioMetros, LocalDateTime tiempoLimite) {
        this.lat = lat;
        this.lng = lng;
        this.radioMetros = radioMetros;
        this.tiempoLimite = tiempoLimite;
    }
}

