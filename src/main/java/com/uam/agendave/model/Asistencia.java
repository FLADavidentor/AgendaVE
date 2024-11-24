package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Asistencia extends Identifiable{


    @OneToOne
    @JoinColumn(name = "idActividad")
    private Actividad actividad;

    @OneToMany(mappedBy = "asistencia")
    private List<DetalleAsistencia> detalles;

    // Getters y Setters
}