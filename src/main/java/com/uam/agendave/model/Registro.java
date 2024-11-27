package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
public class Registro extends Identifiable {

    private boolean convalidacion; // Indica si el registro incluye convalidación
    private boolean transporte;    // Indica si el estudiante usará transporte

    @ManyToOne
    @JoinColumn(name = "idEstudiante", nullable = false)
    private Estudiante estudiante; // Relación con el estudiante

    @ManyToOne
    @JoinColumn(name = "idActividad", nullable = false)
    private Actividad actividad;   // Relación con la actividad

    @ElementCollection
    @CollectionTable(name = "registro_convalidaciones", joinColumns = @JoinColumn(name = "idRegistro"))
    @MapKeyColumn(name = "tipoConvalidacion") // Enum como clave
    @Column(name = "cantidad")
    @Enumerated(EnumType.STRING) // Guardar el enum como String en la base de datos
    private Map<TipoConvalidacion, Integer> convalidacionesRealizadas; // Créditos convalidados por tipo

    @Column(name = "totalConvalidado")
    private int totalConvalidado; // Créditos totales convalidados por el estudiante en esta actividad

    }


