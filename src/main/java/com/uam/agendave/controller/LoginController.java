package com.uam.agendave.controller;

import com.uam.agendave.dto.LoginRequestUser;
import com.uam.agendave.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticarUsuario(@RequestBody LoginRequestUser loginRequestUser) {
        String token = usuarioService.autenticarUsuario(loginRequestUser.getUsername(), loginRequestUser.getPassword());
        return ResponseEntity.ok(token);
    }
}
