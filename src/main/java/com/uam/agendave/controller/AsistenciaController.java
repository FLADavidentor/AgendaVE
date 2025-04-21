package com.uam.agendave.controller;

import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.Registro;
import com.uam.agendave.service.ActividadService;  // Asegúrate de importar esta clase
import com.uam.agendave.service.RegistroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asistencia")
@CrossOrigin(origins = "http://localhost:5173")   // permite llamadas desde tu front
public class AsistenciaController {

    private final RegistroService registroService;
    private final ActividadService actividadService;

    // Inyección de los servicios en el constructor
    public AsistenciaController(RegistroService registroService, ActividadService actividadService) {
        this.registroService = registroService;
        this.actividadService = actividadService;  // Asignamos el servicio de Actividad
    }

//    // Obtener todas las asistencias
//    @GetMapping("/all")
//    public List<Asistencia> obtenerTodas() {
//        return asistenciaService.obtenerTodas();
//    }

    // Tiene que llegar un DTO que contenga el CIF del estudiante y el ID de la actividad
    // Como parametros me tienen que mandar un: Cif: String - Id de Actividad: String - Transporte:Boolean
    @PostMapping("/confirmar_inscripcion")
    public ResponseEntity inscribirActividad(@RequestBody RegistroDTO registroDTO) {

        try {
            registroService.guardarRegistro(registroDTO, actividadService);
        } catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(201).body(null);
    }


//    // Obtener asistencia por actividad
//    @GetMapping("/actividad/{actividadId}")
//    public ResponseEntity<List<Asistencia>> obtenerPorActividad(@PathVariable UUID actividadId) {
//        List<Asistencia> asistencias = asistenciaService.buscarPorActividadId(actividadId);
//        return ResponseEntity.ok(asistencias);
//    }

    // Crear nueva asistencia
//    @PostMapping("/create")
//    public ResponseEntity<Asistencia> crearAsistencia(@RequestBody AsistenciaDTO asistenciaDTO) {
//        // Convertir DTO a entidad
//        Asistencia nuevaAsistencia = new Asistencia();
//
//        // Buscar la actividad por ID (este es el cambio)
//        Actividad actividad = actividadService.buscarPorId(asistenciaDTO.getIdActividad());  // Aquí usamos el servicio de Actividad
//
//        // Asignamos la actividad a la nueva asistencia
//        nuevaAsistencia.setActividad(actividad);  // Establecer el objeto Actividad completo en vez de solo el ID
//
//        // Guardar asistencia
//        Asistencia asistenciaGuardada = asistenciaService.guardarAsistencia(nuevaAsistencia);
//        return ResponseEntity.status(201).body(asistenciaGuardada);
//    }

//    // Eliminar asistencia por ID
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> eliminarAsistencia(@PathVariable UUID id) {
//        asistenciaService.eliminarAsistencia(id);
//        return ResponseEntity.noContent().build();
//    }
}

