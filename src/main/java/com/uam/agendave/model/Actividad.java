package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Actividad extends Identifiable {

    @ManyToOne
    @JoinColumn(name = "idNombreActividad", nullable = false) // Relación con NombreActividad
    private NombreActividad nombreActividad;
    @Column(nullable = false)
    private String nombre; // Este campo se llena automáticamente desde NombreActividad
    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Temporal(TemporalType.TIME)
    private Time horaInicio;

    @Temporal(TemporalType.TIME)
    private Time horaFin;

    private boolean estado;
    private int cupo;

    @ElementCollection
    @CollectionTable(name = "actividad_convalidaciones", joinColumns = @JoinColumn(name = "idActividad"))
    @MapKeyColumn(name = "tipoConvalidacion") // Enum como clave
    @Column(name = "cantidadPermitida")
    @Enumerated(EnumType.STRING) // Guardar el enum como String en la base de datos
    private Map<TipoConvalidacion, Integer> convalidacionesPermitidas;


    private Integer totalConvalidacionesPermitidas;

//    @OneToMany(mappedBy = "actividad", fetch = FetchType.LAZY)
//    private List<Registro> registros;

    @ManyToOne
    @JoinColumn(name = "idLugar")
    private Lugar lugar;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Administrador usuario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(name = "imagen_id", nullable = true)
    private ImageData imagen;



}