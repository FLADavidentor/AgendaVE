package com.uam.agendave.controller;

import com.uam.agendave.dto.NombreActividadDTO;
import com.uam.agendave.mapper.NombreActividadMapper;
import com.uam.agendave.model.NombreActividad;
import com.uam.agendave.service.NombreActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/nombre-actividades")
public class NombreActividadController {

    private final NombreActividadService nombreActividadService;
    private final NombreActividadMapper nombreActividadMapper;

    public NombreActividadController(NombreActividadService nombreActividadService) {
        this.nombreActividadService = nombreActividadService;
        this.nombreActividadMapper = new NombreActividadMapper();
    }

    // Obtener todas las actividades por nombre exacto
    @PreAuthorize("hasRole('ADMIN')")
    
    @GetMapping("/buscar")
    public ResponseEntity<List<NombreActividadDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<NombreActividad> actividades = nombreActividadService.buscarPorNombre(nombre);
        return ResponseEntity.ok(actividades.stream()
                .map(nombreActividadMapper::toDTO)
                .collect(Collectors.toList()));
    }

    // Buscar actividades por coincidencia parcial del nombre
    @PreAuthorize("hasRole('ADMIN')")
    
    @GetMapping("/buscar-parcial")
    public ResponseEntity<List<NombreActividadDTO>> buscarPorNombreParcial(@RequestParam String parteDelNombre) {
        List<NombreActividad> actividades = nombreActividadService.buscarPorNombreParcial(parteDelNombre);
        return ResponseEntity.ok(actividades.stream()
                .map(nombreActividadMapper::toDTO)
                .collect(Collectors.toList()));
    }

    // Crear una nueva NombreActividad
    @PreAuthorize("hasRole('ADMIN')")
    
    @PostMapping("/create")
    public ResponseEntity<NombreActividadDTO> crearNombreActividad(@RequestBody NombreActividadDTO nombreActividadDTO) {
        NombreActividad nombreActividad = nombreActividadMapper.toEntity(nombreActividadDTO);
        NombreActividad nuevaActividad = nombreActividadService.guardar(nombreActividad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nombreActividadMapper.toDTO(nuevaActividad));
    }


}
