package com.uam.agendave.controller;

import com.uam.agendave.dto.Usuario.EstudianteDTO;
import com.uam.agendave.dto.Usuario.LoginRequest;
import com.uam.agendave.service.usuario.UsuarioService;
import com.uam.agendave.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
