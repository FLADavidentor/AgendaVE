package com.uam.agendave.service.actividad;

import com.uam.agendave.dto.Actividad.ActividadDTO;
import com.uam.agendave.dto.Usuario.EstudianteDTO;
import com.uam.agendave.model.Actividad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ActividadService {

    List<ActividadDTO> obtenerTodas();

    void guardarActividad(ActividadDTO actividadDTO) throws Exception;

    Actividad buscarPorId(UUID id);

    ActividadDTO actualizarActividad(ActividadDTO actividadDTO);

    void eliminarActividad(UUID id);

    int getCupoRestante(UUID actividadID);

    List<EstudianteDTO> obtenerListadoEstudiante(UUID idActividad);

    Page<ActividadDTO> obtenerActividades(Pageable pageable);

    List<ActividadDTO> obtenerActividadesActivas();


    List<ActividadDTO> obtenerActividadesPorNombre(String nombre);
}
