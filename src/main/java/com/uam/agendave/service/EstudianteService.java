package com.uam.agendave.service;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.model.Estudiante;

import java.util.List;
import java.util.UUID;

public interface EstudianteService {
    String autenticarEstudiante(LoginRequest loginRequest);

    String obtenerInformacionEstudiante(String token, LoginRequest loginRequest);
}

