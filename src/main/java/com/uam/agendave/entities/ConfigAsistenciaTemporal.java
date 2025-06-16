package com.uam.agendave.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class ConfigAsistenciaTemporal {
    private Double radioMetros;
    private LocalDateTime tiempoLimite;

    public ConfigAsistenciaTemporal(Double radioMetros, LocalDateTime tiempoLimite) {
        this.radioMetros = radioMetros;
        this.tiempoLimite = tiempoLimite;
    }
}

