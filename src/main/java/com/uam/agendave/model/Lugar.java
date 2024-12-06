package com.uam.agendave.model;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Lugar extends Identifiable {

    private String nombre;
    private int capacidad;

    @OneToMany(mappedBy = "lugar")
    private List<Actividad> actividades;
}
