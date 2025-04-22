package com.uam.agendave.controller;

import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.dto.LoginRequestUser;
import com.uam.agendave.model.Usuario;
import com.uam.agendave.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class LoginController {

    private final UsuarioService estudianteService;

    public LoginController(UsuarioService estudianteService) {
        this.estudianteService = estudianteService;
    }


    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> obtenerInformacionEstudiante(@RequestBody LoginRequest loginRequest) {
        String token = estudianteService.autenticarEstudiante(loginRequest);
        String estudianteData = estudianteService.obtenerInformacionEstudiante(token, loginRequest);
        return ResponseEntity.ok(estudianteData);
    }
}
