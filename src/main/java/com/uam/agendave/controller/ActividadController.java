package com.uam.agendave.controller;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.service.ActividadService;
import com.uam.agendave.service.ActivityCleanupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/actividad")
public class ActividadController {
    private final ActividadService actividadService;
    private final ActivityCleanupService activityCleanupService;

    public ActividadController(ActividadService actividadService, ActivityCleanupService activityCleanupService) {
        this.actividadService = actividadService;
        this.activityCleanupService = activityCleanupService;
    }

    @GetMapping("/all")
    public List<ActividadDTO> obtenerTodas() {
        return actividadService.obtenerTodas();
    }

    @GetMapping("/get_active")
    public List<ActividadDTO> obtenerActividad() {
        return actividadService.obtenerActividadesActivas();
    }

    @GetMapping("/get_actividad_nombre")
    public List<ActividadDTO> obtenerActividadNombre(@RequestParam String nombre) {
        try {
            return actividadService.obtenerActividadesPorNombre(nombre);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    public void crearActividad(@RequestBody ActividadDTO actividadDTO) {
        try {
            actividadService.guardarActividad(actividadDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}/cupo_restante")
    public int cupoRestante(@PathVariable UUID id) {
        return actividadService.getCupoRestante(id);
    }

    @GetMapping("/reiniciar_estado")
    public void reiniciarEstado() {
        activityCleanupService.deactivatePastActivities();
    }

    @PostMapping("/listado_estudiante/{id_actividad}")
    public List<EstudianteDTO> listadoActividad(@PathVariable UUID id_actividad) {
        try {
            return actividadService.obtenerListadoEstudiante(id_actividad);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/obtener_actividades")
    public ResponseEntity<Page<ActividadDTO>> obtenerActividades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction dir
    ) {

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));
            Page<ActividadDTO> resultado = actividadService.obtenerActividades(pageable);

            return ResponseEntity.status(HttpStatus.OK).body(resultado);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    // Actualizar actividad existente
    @CrossOrigin(origins = "*")
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


}
