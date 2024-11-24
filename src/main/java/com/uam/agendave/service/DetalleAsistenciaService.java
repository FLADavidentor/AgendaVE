package com.uam.agendave.service;

import com.uam.agendave.model.DetalleAsistencia;

import java.util.List;
import java.util.UUID;

public interface DetalleAsistenciaService {
    List<DetalleAsistencia> obtenerTodos();
    DetalleAsistencia guardarDetalleAsistencia(DetalleAsistencia detalleAsistencia);
    DetalleAsistencia buscarPorId(UUID id);
    void eliminarDetalleAsistencia(UUID id);
    List<DetalleAsistencia> obtenerPorAsistenciaId(UUID asistenciaId);
    List<DetalleAsistencia> obtenerPorRegistroId(UUID registroId);
}
