package com.uam.agendave.service;

import com.uam.agendave.model.Asistencia;

import java.util.List;
import java.util.UUID;

public interface AsistenciaService {
    Asistencia guardarAsistencia(Asistencia asistencia);                // Guardar una nueva asistencia
    Asistencia buscarPorId(UUID id);                                     // Buscar asistencia por ID
    void eliminarAsistencia(UUID id);                                    // Eliminar una asistencia por ID
    List<Asistencia> obtenerTodas();                                     // Obtener todas las asistencias
    List<Asistencia> buscarPorActividadId(UUID actividadId);             // Buscar asistencias por ID de actividad
}
