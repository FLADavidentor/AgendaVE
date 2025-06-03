package com.uam.agendave.controller;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.mapper.EstudianteMapper;
import com.uam.agendave.model.Usuario;
import com.uam.agendave.model.Estudiante;
import com.uam.agendave.repository.UsuarioRepository;
import com.uam.agendave.repository.EstudianteRepository;
import com.uam.agendave.service.UsuarioService;
import com.uam.agendave.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> loginEstudiante(@RequestBody LoginRequest loginRequest) {
        try {
            EstudianteDTO dto = usuarioService.loginUsuario(loginRequest);
            String token = jwtUtil.generateToken(dto.getCif(), "ESTUDIANTE");

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "rol", "ESTUDIANTE",
                    "usuario", dto
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login_admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {
        try {
            return usuarioService.loginUsuarioAdmin(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
}
