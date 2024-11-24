package com.uam.agendave.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class TipoActividad extends Identifiable {

    private String nombreTipo;
    private String facultadEncargada;

    @OneToMany(mappedBy = "tipoActividad")
    private List<Actividad> actividades;
}
