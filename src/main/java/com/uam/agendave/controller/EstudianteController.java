package com.uam.agendave.controller;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.model.Estudiante;
import com.uam.agendave.service.EstudianteService;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estudiante")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }


    @PostMapping("/login")
    public ResponseEntity<String> obtenerInformacionEstudiante(@RequestBody LoginRequest loginRequest) {
        String token = estudianteService.autenticarEstudiante(loginRequest);
        String estudianteData = estudianteService.obtenerInformacionEstudiante(token, loginRequest);
        return ResponseEntity.ok(estudianteData);
    }

}

