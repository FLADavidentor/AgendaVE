package com.uam.agendave.controller;

import com.uam.agendave.dto.TipoActividadDTO;
import com.uam.agendave.service.TipoActividadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tipo-actividades")
public class TipoActividadController {

    private final TipoActividadService tipoActividadService;

    public TipoActividadController(TipoActividadService tipoActividadService) {
        this.tipoActividadService = tipoActividadService;
    }

    // Obtener todos los tipos de actividades
    @GetMapping("/all")
    public List<TipoActividadDTO> obtenerTodos() {
        return tipoActividadService.obtenerTodos();
    }

    // Buscar tipo de actividad por ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoActividadDTO> buscarPorId(@PathVariable UUID id) {
        TipoActividadDTO tipoActividadDTO = tipoActividadService.buscarPorId(id);
        if (tipoActividadDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tipoActividadDTO);
    }

    // Crear nuevo tipo de actividad
    @PostMapping("/create")
    public ResponseEntity<TipoActividadDTO> guardarTipoActividad(@RequestBody TipoActividadDTO tipoActividadDTO) {
        TipoActividadDTO nuevoTipoActividad = tipoActividadService.guardarTipoActividad(tipoActividadDTO);
        return ResponseEntity.status(201).body(nuevoTipoActividad);
    }

    // Eliminar tipo de actividad por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarTipoActividad(@PathVariable UUID id) {
        tipoActividadService.eliminarTipoActividad(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar tipos de actividades por nombre
    @GetMapping("/search-by-name/{nombre}")
    public List<TipoActividadDTO> buscarPorNombre(@PathVariable String nombre) {
        return tipoActividadService.buscarPorNombre(nombre);
    }

    // Buscar tipos de actividades por facultad encargada
    @GetMapping("/search-by-faculty/{facultad}")
    public List<TipoActividadDTO> buscarPorFacultadEncargada(@PathVariable String facultad) {
        return tipoActividadService.buscarPorFacultadEncargada(facultad);
    }
    @GetMapping("/search-by-name-partial")
    public List<TipoActividadDTO> buscarPorNombreParcial(@RequestParam String parteDelNombre) {
        return tipoActividadService.buscarPorNombreParcial(parteDelNombre);
    }

}