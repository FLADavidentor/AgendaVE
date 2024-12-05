package com.uam.agendave.controller;

import com.uam.agendave.dto.NombreActividadDTO;
import com.uam.agendave.dto.TipoActividadDTO;
import com.uam.agendave.model.NombreActividad;
import com.uam.agendave.model.TipoActividad;
import com.uam.agendave.service.NombreActividadService;
import com.uam.agendave.service.TipoActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nombre-actividades")
public class NombreActividadController {

    private final NombreActividadService nombreActividadService;

    public NombreActividadController(NombreActividadService nombreActividadService, TipoActividadService tipoActividadService) {
        this.nombreActividadService = nombreActividadService;
    }

    // Obtener todas las actividades por nombre exacto
    @GetMapping("/buscar")
    public ResponseEntity<List<NombreActividadDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<NombreActividad> actividades = nombreActividadService.buscarPorNombre(nombre);
        return ResponseEntity.ok(actividades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList()));
    }

    // Buscar actividades por coincidencia parcial del nombre
    @GetMapping("/buscar-parcial")
    public ResponseEntity<List<NombreActividadDTO>> buscarPorNombreParcial(@RequestParam String parteDelNombre) {
        List<NombreActividad> actividades = nombreActividadService.buscarPorNombreParcial(parteDelNombre);
        return ResponseEntity.ok(actividades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList()));
    }

    // Crear una nueva NombreActividad
    @PostMapping("/create")
    public ResponseEntity<NombreActividadDTO> crearNombreActividad(@RequestBody NombreActividadDTO nombreActividadDTO) {
        NombreActividad nombreActividad = convertirAEntidad(nombreActividadDTO);
        NombreActividad nuevaActividad = nombreActividadService.guardar(nombreActividad); // Este método debe llamarse
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(nuevaActividad));
    }

    // Métodos auxiliares para la conversión entre DTO y entidad
    private NombreActividadDTO convertirADTO(NombreActividad nombreActividad) {
        NombreActividadDTO dto = new NombreActividadDTO();
        dto.setId(nombreActividad.getId());
        dto.setNombre(nombreActividad.getNombre());
        return dto;
    }

    private NombreActividad convertirAEntidad(NombreActividadDTO dto) {
        NombreActividad nombreActividad = new NombreActividad();
        nombreActividad.setId(dto.getId());
        nombreActividad.setNombre(dto.getNombre());

        return nombreActividad;
    }

}
