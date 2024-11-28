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
    public ResponseEntity<String> autenticarEstudiante(@RequestBody LoginRequest loginRequest) {
        String token = estudianteService.autenticarEstudiante(loginRequest.getCif(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{cif}")
    public ResponseEntity<Estudiante> obtenerInformacionEstudiante(
            @PathVariable String cif, @RequestHeader("Authorization") String token) {

        // Logs de entrada
        System.out.println("CIF recibido: " + cif);
        System.out.println("Token recibido en header: " + token);

        Estudiante estudiante = estudianteService.obtenerInformacionEstudiante(cif, token);
        return ResponseEntity.ok(estudiante);
    }
}

