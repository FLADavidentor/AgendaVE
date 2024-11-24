package com.uam.agendave.controller;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.service.EstudianteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public List<EstudianteDTO> obtenerTodos() {
        return estudianteService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public EstudianteDTO obtenerPorId(@PathVariable UUID id) {
        return estudianteService.buscarPorId(id);
    }

    @PostMapping
    public EstudianteDTO crearEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
        return estudianteService.guardarEstudiante(estudianteDTO);
    }

    @PutMapping("/{id}")
    public EstudianteDTO actualizarEstudiante(@PathVariable UUID id, @RequestBody EstudianteDTO estudianteDTO) {
        estudianteDTO.setId(id);
        return estudianteService.guardarEstudiante(estudianteDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminarEstudiante(@PathVariable UUID id) {
        estudianteService.eliminarEstudiante(id);
    }
}
