package com.uam.agendave.controller;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.service.ActividadService;
import com.uam.agendave.service.LugarService;
import com.uam.agendave.service.NombreActividadService;
import com.uam.agendave.service.RegistroService;
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
    private final NombreActividadService nombreActividadService;
    private final LugarService lugarService;


    // Constructor
    public ActividadController(ActividadService actividadService,
                               NombreActividadService nombreActividadService,
                               LugarService lugarService) {
        this.actividadService = actividadService;
        this.nombreActividadService = nombreActividadService;
        this.lugarService = lugarService;
    }



    @GetMapping("/all")
    public List<ActividadDTO> obtenerTodas() {
        return actividadService.obtenerTodas();
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    public void crearActividad(@RequestBody ActividadDTO actividadDTO) {
        try {
            actividadService.guardarActividad(actividadDTO);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}/cupo_restante")
    public int cupoRestante(@PathVariable UUID id) {
        return actividadService.getCupoRestante(id);
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


    @PostMapping("/listado_estudiante/{id_actividad}")
    public List<EstudianteDTO> listadoActividad(@PathVariable UUID id_actividad) {
        try {
            return actividadService.obtenerListadoEstudiante(id_actividad);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/obtener_actividades/{id_actividad}/{paginacion}")
    public List<ActividadDTO> obtenerActividades(@PathVariable UUID id_actividad, @PathVariable int paginacion) {

        try {
            return actividadService.obtenerActividadesXPaginacion(id_actividad, paginacion);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }


    }



//    // Buscar actividades con cupo disponible
//    @GetMapping("/con-cupo")
//    public List<ActividadDTO> obtenerActividadesConCupoDisponible() {
//        return
}
