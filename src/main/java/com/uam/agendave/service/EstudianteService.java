package com.uam.agendave.service;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.model.Estudiante;

import java.util.List;
import java.util.UUID;

public interface EstudianteService {
    String autenticarEstudiante(String cif, String password);

    Estudiante obtenerInformacionEstudiante(String cif, String token);
}

