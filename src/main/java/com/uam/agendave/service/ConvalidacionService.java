package com.uam.agendave.service;

import com.uam.agendave.dto.ConvalidacionDTO;

import java.util.List;
import java.util.UUID;

public interface ConvalidacionService {
    void convalidarCreditos(UUID idDetalleAsistencia); // Reglas de negocio principales
    List<ConvalidacionDTO> obtenerPorEstudiante(String idEstudiante); // Consultar créditos por estudiante
    List<ConvalidacionDTO> obtenerPorActividad(UUID idActividad);   // Consultar créditos por actividad
}
