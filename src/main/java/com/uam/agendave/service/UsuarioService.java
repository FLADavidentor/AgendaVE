package com.uam.agendave.service;

import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.dto.UsuarioDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {


    String autenticarEstudiante(LoginRequest loginRequest);

    String obtenerInformacionEstudiante(String token, LoginRequest loginRequest);
}
