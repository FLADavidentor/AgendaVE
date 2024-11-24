package com.uam.agendave.service;

import com.uam.agendave.dto.EstudianteDTO;
import java.util.List;
import java.util.UUID;

public interface EstudianteService {
    List<EstudianteDTO> obtenerTodos();
    EstudianteDTO guardarEstudiante(EstudianteDTO estudianteDTO);
    EstudianteDTO buscarPorId(UUID id);
    List<EstudianteDTO> buscarPorNombreYApellido(String nombre, String apellido);
    List<EstudianteDTO> buscarPorCif(String cif);
    void eliminarEstudiante(UUID id);
}
