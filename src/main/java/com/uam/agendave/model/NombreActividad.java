package com.uam.agendave.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NombreActividad extends Identifiable {

    private String nombre;

    @ManyToOne
    private TipoActividad tipoActividad; // Relación opcional con TipoActividad
}
