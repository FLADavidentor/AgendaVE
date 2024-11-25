package com.uam.agendave.service;

import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Registro;

import java.util.List;
import java.util.UUID;

public interface RegistroService {

    List<RegistroDTO> obtenerTodos();

    RegistroDTO guardarRegistro(RegistroDTO registroDTO);

    RegistroDTO buscarPorId(UUID id);

    void eliminarRegistro(UUID id);

    List<RegistroDTO> buscarPorEstudiante(UUID idEstudiante);

    List<RegistroDTO> buscarPorActividad(UUID idActividad);
    Registro convertirADetalleAsistencia(RegistroDTO registroDTO);

}

