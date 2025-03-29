package com.uam.agendave.dto;

import com.uam.agendave.model.TipoConvalidacion;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ActividadDTO {

    private UUID id;
    private String descripcion;
    private Date fecha;
    private Time horaInicio;
    private Time horaFin;
    private boolean estado;
    private int cupo;

    private String nombreActividad; // Enviar este nombre al endpoint
    private String lugar;           // Enviar este nombre al endpoint

    private Map<TipoConvalidacion, Integer> convalidacionesPermitidas; // Convalidaciones por tipo
    private Integer totalConvalidacionesPermitidas;
}
