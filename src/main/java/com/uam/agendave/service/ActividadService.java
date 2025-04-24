package com.uam.agendave.service;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.dto.EstudianteDTO;
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

    int getCupoRestante(UUID actividadID);

    List<EstudianteDTO> obtenerListadoEstudiante(UUID idActividad);

    List<ActividadDTO> obtenerActividadesXPaginacion(UUID idActividad, int paginacion);
}
