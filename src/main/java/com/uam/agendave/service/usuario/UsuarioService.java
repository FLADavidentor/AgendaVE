package com.uam.agendave.service.usuario;

import com.uam.agendave.dto.Usuario.EstudianteDTO;
import com.uam.agendave.dto.Usuario.LoginRequest;
import com.uam.agendave.dto.Usuario.TestDTO;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    EstudianteDTO loginUsuario(LoginRequest loginRequest);

    String autenticarEstudiante(LoginRequest loginRequest);

    TestDTO obtenerInformacionEstudiante(String token, LoginRequest loginRequest);

    ResponseEntity<?> loginUsuarioAdmin(LoginRequest loginRequest);
}
