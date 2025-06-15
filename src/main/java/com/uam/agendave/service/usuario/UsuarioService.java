package com.uam.agendave.service.usuario;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.dto.TestDTO;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    EstudianteDTO loginUsuario(LoginRequest loginRequest);

    String autenticarEstudiante(LoginRequest loginRequest);

    TestDTO obtenerInformacionEstudiante(String token, LoginRequest loginRequest);

    ResponseEntity<?> loginUsuarioAdmin(LoginRequest loginRequest);
}
