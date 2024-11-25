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

    private UUID id; // Heredado de Identifiable
    private String descripcion;
    private Date fecha;
    private Time horaInicio;
    private Time horaFin;
    private boolean estado;
    private int cupo;

    private UUID idNombreActividad; // Relación con NombreActividad
    private UUID idLugar;           // Relación con Lugar
    private UUID idTipoActividad;   // Relación con TipoActividad
    private UUID idUsuario;         // Relación con Usuario

    private Map<TipoConvalidacion, Integer> convalidacionesPermitidas; // Convalidaciones por tipo
    private Integer totalConvalidacionesPermitidas;
}
