package com.uam.agendave.controller;

import com.uam.agendave.dto.ActividadDTO;
import com.uam.agendave.dto.LugarDTO;
import com.uam.agendave.dto.TipoActividadDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.NombreActividad;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.service.ActividadService;
import com.uam.agendave.service.LugarService;
import com.uam.agendave.service.NombreActividadService;
import com.uam.agendave.service.TipoActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/actividad")
public class ActividadController {
    private final ActividadService actividadService;
    private final NombreActividadService nombreActividadService;
    private final LugarService lugarService;
    private final TipoActividadService tipoActividadService;

    // Constructor
    public ActividadController(ActividadService actividadService,
                               NombreActividadService nombreActividadService,
                               LugarService lugarService,
                               TipoActividadService tipoActividadService) {
        this.actividadService = actividadService;
        this.nombreActividadService = nombreActividadService;
        this.lugarService = lugarService;
        this.tipoActividadService = tipoActividadService;
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

    @PostMapping("/create")
    public ResponseEntity<ActividadDTO> crearActividad(@RequestBody ActividadDTO actividadDTO) {
        try {
            // Buscar y asignar el ID de NombreActividad
            List<NombreActividad> nombreActividades = nombreActividadService.buscarPorNombre(actividadDTO.getNombreActividad());
            if (!nombreActividades.isEmpty()) {
                actividadDTO.setNombreActividad(nombreActividades.get(0).getNombre()); // Usa el primero que encuentra
            } else {
                throw new IllegalArgumentException("NombreActividad no encontrado: " + actividadDTO.getNombreActividad());
            }

            // Buscar y asignar el ID de Lugar
            List<LugarDTO> lugares = lugarService.buscarPorNombre(actividadDTO.getLugar());
            if (!lugares.isEmpty()) {
                actividadDTO.setLugar(lugares.get(0).getNombre());
            } else {
                throw new IllegalArgumentException("Lugar no encontrado: " + actividadDTO.getLugar());
            }

            // Buscar y asignar el ID de TipoActividad
            List<TipoActividadDTO> tiposActividad = tipoActividadService.buscarPorNombre(actividadDTO.getTipoActividad());
            if (!tiposActividad.isEmpty()) {
                actividadDTO.setTipoActividad(tiposActividad.get(0).getNombreTipo());
            } else {
                throw new IllegalArgumentException("TipoActividad no encontrado: " + actividadDTO.getTipoActividad());
            }

            // Guardar la actividad usando el servicio
            ActividadDTO actividadCreada = actividadService.guardarActividad(actividadDTO);
            return ResponseEntity.status(201).body(actividadCreada);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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
    @GetMapping("/{id}/convalidaciones")
    public ResponseEntity<Map<TipoConvalidacion, Integer>> obtenerConvalidaciones(@PathVariable UUID id) {
        try {
            Map<TipoConvalidacion, Integer> convalidaciones = actividadService.obtenerConvalidacionesPorActividad(id);
            return ResponseEntity.ok(convalidaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{id}/convalidaciones/total")
    public ResponseEntity<Integer> obtenerTotalConvalidaciones(@PathVariable UUID id) {
        return ResponseEntity.ok(actividadService.obtenerTotalConvalidacionesMaximas(id));
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
            actividadDTO.setNombreActividad(actividad.getNombreActividad().getNombre());
        }
        if (actividad.getLugar() != null) {
            actividadDTO.setLugar(actividad.getLugar().getNombre());
        }
        if (actividad.getTipoActividad() != null) {
            actividadDTO.setTipoActividad(actividad.getTipoActividad().getNombreTipo());
        }

        actividadDTO.setConvalidacionesPermitidas(actividad.getConvalidacionesPermitidas());
        actividadDTO.setTotalConvalidacionesPermitidas(actividad.getTotalConvalidacionesPermitidas());

        return actividadDTO;
    }

}
