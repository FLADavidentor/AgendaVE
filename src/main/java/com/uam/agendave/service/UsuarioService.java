package com.uam.agendave.service;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.dto.TestDTO;
import com.uam.agendave.dto.UsuarioDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {

    EstudianteDTO loginUsuario(LoginRequest loginRequest);

    String autenticarEstudiante(LoginRequest loginRequest);

    TestDTO obtenerInformacionEstudiante(String token, LoginRequest loginRequest);

    ResponseEntity<?> loginUsuarioAdmin(LoginRequest loginRequest);
}
