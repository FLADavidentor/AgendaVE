package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DetalleAsistencia extends Identifiable{

    @Enumerated(EnumType.STRING)
    private EstadoAsistencia estadoAsistencia;

    @ManyToOne
    @JoinColumn(name = "idAsistencia")
    private Asistencia asistencia;

    @ManyToOne
    @JoinColumn(name = "idRegistro")
    private Registro registro;

}
