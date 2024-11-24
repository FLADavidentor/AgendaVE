package com.uam.agendave.controller;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.service.ActividadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    // Obtener todas las actividades
    @GetMapping("/all")
    public List<ActividadDTO> obtenerTodas() {
        return actividadService.obtenerTodas();
    }

    // Obtener actividad por ID
    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> obtenerPorId(@PathVariable UUID id) {
        Actividad actividad = actividadService.buscarPorId(id);
        if (actividad == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAModelDTO(actividad));
    }

    // Crear nueva actividad
    @PostMapping("/create")
    public ResponseEntity<ActividadDTO> crearActividad(@RequestBody ActividadDTO actividadDTO) {
        ActividadDTO actividadCreada = actividadService.guardarActividad(actividadDTO);
        return ResponseEntity.status(201).body(actividadCreada);
    }

    // Actualizar actividad existente
    @PutMapping("/update/{id}")
    public ResponseEntity<ActividadDTO> actualizarActividad(@PathVariable UUID id, @RequestBody ActividadDTO actividadDTO) {
        actividadDTO.setId(id);
        ActividadDTO actividadActualizada = actividadService.actualizarActividad(actividadDTO);
        if (actividadActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actividadActualizada);
    }

    // Eliminar actividad
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable UUID id) {
        actividadService.eliminarActividad(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar actividades con cupo disponible
    @GetMapping("/con-cupo")
    public List<ActividadDTO> obtenerActividadesConCupoDisponible() {
        return actividadService.buscarActividadesConCupoDisponible();
    }

    // Conversión de Actividad a ActividadDTO
    private ActividadDTO convertirAModelDTO(Actividad actividad) {
        ActividadDTO actividadDTO = new ActividadDTO();
        actividadDTO.setId(actividad.getId());
        actividadDTO.setDescripcion(actividad.getDescripcion());
        actividadDTO.setFecha(actividad.getFecha());
        actividadDTO.setHoraInicio(actividad.getHoraInicio());
        actividadDTO.setHoraFin(actividad.getHoraFin());
        actividadDTO.setEstado(actividad.isEstado());
        actividadDTO.setCupo(actividad.getCupo());

        // Relaciones
        if (actividad.getNombreActividad() != null) {
            actividadDTO.setIdNombreActividad(actividad.getNombreActividad().getId());
        }
        if (actividad.getLugar() != null) {
            actividadDTO.setIdLugar(actividad.getLugar().getId());
        }
        if (actividad.getTipoActividad() != null) {
            actividadDTO.setIdTipoActividad(actividad.getTipoActividad().getId());
        }
        if (actividad.getUsuario() != null) {
            actividadDTO.setIdUsuario(actividad.getUsuario().getId());
        }

        actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

        return actividadDTO;
    }
}
