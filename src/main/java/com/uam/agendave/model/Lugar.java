package com.uam.agendave.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Double latitud;
    private Double longitud;

    @OneToMany(mappedBy = "lugar")
    @JsonBackReference
    private List<Actividad> actividades;
}
