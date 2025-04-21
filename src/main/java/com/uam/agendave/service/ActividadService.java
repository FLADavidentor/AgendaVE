package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.TipoConvalidacion;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ActividadService {

    List<ActividadDTO> obtenerTodas();

    void guardarActividad(ActividadDTO actividadDTO) throws Exception;

    Actividad buscarPorId(UUID id);

    ActividadDTO actualizarActividad(ActividadDTO actividadDTO);

    void eliminarActividad(UUID id);

    List<ActividadDTO> buscarPorNombre(String nombre);

    List<ActividadDTO> buscarPorLugar(UUID idLugar);

    int getCupoRestante(UUID actividadID);

    List<ActividadDTO> buscarActividadesConCupoDisponible(); // Ajustado para no requerir el parámetro cupo
    Map<TipoConvalidacion, Integer> obtenerConvalidacionesPorActividad(UUID id);
    Integer obtenerTotalConvalidacionesMaximas(UUID id);

}
