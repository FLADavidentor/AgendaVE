package com.uam.agendave.controller;

import com.uam.agendave.dto.AsistenciaDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.Asistencia;
import com.uam.agendave.service.AsistenciaService;
import com.uam.agendave.service.ActividadService;  // Asegúrate de importar esta clase
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/asistencia")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final ActividadService actividadService;  // Añadir esta línea para inyectar ActividadService

    // Inyección de los servicios en el constructor
    public AsistenciaController(AsistenciaService asistenciaService, ActividadService actividadService) {
        this.asistenciaService = asistenciaService;
        this.actividadService = actividadService;  // Asignamos el servicio de Actividad
    }

    // Obtener todas las asistencias
    @GetMapping("/all")
    public List<Asistencia> obtenerTodas() {
        return asistenciaService.obtenerTodas();
    }


    @PostMapping("/inscribir_actividad")
    public ResponseEntity inscribirActividad(@RequestBody )




    // Obtener asistencia por actividad
    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<Asistencia>> obtenerPorActividad(@PathVariable UUID actividadId) {
        List<Asistencia> asistencias = asistenciaService.buscarPorActividadId(actividadId);
        return ResponseEntity.ok(asistencias);
    }

    // Crear nueva asistencia
    @PostMapping("/create")
    public ResponseEntity<Asistencia> crearAsistencia(@RequestBody AsistenciaDTO asistenciaDTO) {
        // Convertir DTO a entidad
        Asistencia nuevaAsistencia = new Asistencia();

        // Buscar la actividad por ID (este es el cambio)
        Actividad actividad = actividadService.buscarPorId(asistenciaDTO.getIdActividad());  // Aquí usamos el servicio de Actividad

        // Asignamos la actividad a la nueva asistencia
        nuevaAsistencia.setActividad(actividad);  // Establecer el objeto Actividad completo en vez de solo el ID

        // Guardar asistencia
        Asistencia asistenciaGuardada = asistenciaService.guardarAsistencia(nuevaAsistencia);
        return ResponseEntity.status(201).body(asistenciaGuardada);
    }

    // Eliminar asistencia por ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarAsistencia(@PathVariable UUID id) {
        asistenciaService.eliminarAsistencia(id);
        return ResponseEntity.noContent().build();
    }
}

