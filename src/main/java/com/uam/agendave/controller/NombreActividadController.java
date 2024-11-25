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
    private final TipoActividadService tipoActividadService;

    public NombreActividadController(NombreActividadService nombreActividadService, TipoActividadService tipoActividadService) {
        this.nombreActividadService = nombreActividadService;
        this.tipoActividadService = tipoActividadService;
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

    // Buscar actividades por TipoActividad (usando ID)
    @GetMapping("/buscar-por-tipo")
    public ResponseEntity<List<NombreActividadDTO>> buscarPorTipoActividad(@RequestParam UUID idTipoActividad) {
        List<NombreActividad> actividades = nombreActividadService.buscarPorTipoActividad(idTipoActividad);
        return ResponseEntity.ok(actividades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList()));
    }

    // Crear una nueva NombreActividad
    @PostMapping
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
        if (nombreActividad.getTipoActividad() != null) {
            dto.setIdTipoActividad(nombreActividad.getTipoActividad().getId());
        }
        return dto;
    }

    private NombreActividad convertirAEntidad(NombreActividadDTO dto) {
        NombreActividad nombreActividad = new NombreActividad();
        nombreActividad.setId(dto.getId());
        nombreActividad.setNombre(dto.getNombre());

        // Convierte el DTO a TipoActividad y lo asigna
        if (dto.getIdTipoActividad() != null) {
            TipoActividadDTO tipoActividadDTO = tipoActividadService.buscarPorId(dto.getIdTipoActividad());
            TipoActividad tipoActividad = convertirTipoActividadADominio(tipoActividadDTO);
            nombreActividad.setTipoActividad(tipoActividad);
        }

        return nombreActividad;
    }

    // Método auxiliar para convertir TipoActividadDTO a TipoActividad
    private TipoActividad convertirTipoActividadADominio(TipoActividadDTO tipoActividadDTO) {
        TipoActividad tipoActividad = new TipoActividad();
        tipoActividad.setId(tipoActividadDTO.getId());
        tipoActividad.setNombreTipo(tipoActividadDTO.getNombreTipo());
        tipoActividad.setFacultadEncargada(tipoActividadDTO.getFacultadEncargada());
        return tipoActividad;
    }
}
