package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "registro", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idActividad", "cif"})
})
@Getter
@Setter
public class Registro extends Identifiable {

    @Column(nullable = false)
    private String cif;

    @ManyToOne
    @JoinColumn(name = "idActividad", nullable = false)
    private Actividad actividad;   // Relación con la actividad


    private boolean transporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asistencia", nullable = false)
    private EstadoAsistencia estadoAsistencia;

    @Column(name = "totalConvalidado")
    private int totalConvalidado;

    @Column(name = "asistencia_timestamp", nullable = false)
    private LocalDateTime asistenciaTimestamp;

//    @ElementCollection
//    @CollectionTable(name = "registro_convalidaciones", joinColumns = @JoinColumn(name = "idRegistro"))
//    @MapKeyColumn(name = "tipoConvalidacion") // Enum como clave
//    @Column(name = "cantidad")
//    @Enumerated(EnumType.STRING) // Guardar el enum como String en la base de datos
//    private Map<TipoConvalidacion, Integer> convalidacionesRealizadas; // Créditos convalidados por tipo

}


