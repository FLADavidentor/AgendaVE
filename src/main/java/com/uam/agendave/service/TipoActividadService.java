package com.uam.agendave.service;

import com.uam.agendave.dto.TipoActividadDTO;

import java.util.List;
import java.util.UUID;

public interface TipoActividadService {
    List<TipoActividadDTO> obtenerTodos();
    TipoActividadDTO guardarTipoActividad(TipoActividadDTO tipoActividadDTO);
    TipoActividadDTO buscarPorId(UUID id);
    void eliminarTipoActividad(UUID id);
    List<TipoActividadDTO> buscarPorNombre(String nombreTipo);
    List<TipoActividadDTO> buscarPorFacultadEncargada(String facultad);
    List<TipoActividadDTO> buscarPorNombreParcial(String parteDelNombre);
}
