package com.uam.agendave.controller;

import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.dto.LoginRequestUser;
import com.uam.agendave.dto.UsuarioDTO;
import com.uam.agendave.service.UsuarioService;
import com.uam.agendave.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticarUsuario(@RequestBody LoginRequestUser loginRequestUser) {
        String token = usuarioService.autenticarUsuario(loginRequestUser.getUsername(), loginRequestUser.getPassword());
        return ResponseEntity.ok(token);
    }
    @PostMapping("/create")
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO nuevoUsuario = usuarioService.guardarUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
}
