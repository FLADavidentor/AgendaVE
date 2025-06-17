package com.uam.agendave.controller;

import com.uam.agendave.dto.Actividad.ActividadDTO;
import com.uam.agendave.dto.Actividad.LugarDTO;
import com.uam.agendave.dto.Actividad.ResumenActividadDTO;
import com.uam.agendave.dto.EntidadesConfig.ZonaAsistenciaDTO;
import com.uam.agendave.dto.Usuario.EstudianteDTO;
import com.uam.agendave.entities.ConfigAsistenciaTemporal;
import com.uam.agendave.mapper.ActividadMapper;
import com.uam.agendave.service.actividad.ActividadService;
import com.uam.agendave.service.actividad.ActivityCleanupService;
import com.uam.agendave.service.entities.zona.AsistenciaConfigStoreService;
import com.uam.agendave.service.lugar.LugarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/actividad")
public class ActividadController {
    private final ActividadService actividadService;
    private final ActivityCleanupService activityCleanupService;
    private final LugarService lugarService;
    private final AsistenciaConfigStoreService asistenciaConfigStoreService;
    private final ActividadMapper actividadMapper = new ActividadMapper();


    public ActividadController(ActividadService actividadService,
                               ActivityCleanupService activityCleanupService,
                               LugarService lugarService,
                               AsistenciaConfigStoreService asistenciaConfigStoreService) {
        this.actividadService = actividadService;
        this.activityCleanupService = activityCleanupService;
        this.lugarService = lugarService;
        this.asistenciaConfigStoreService = asistenciaConfigStoreService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    
    @GetMapping("/all")
    public List<ActividadDTO> obtenerTodas() {
        return actividadService.obtenerTodas();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    
    @GetMapping("/get_active")
    public List<ActividadDTO> obtenerActividad() {
        return actividadService.obtenerActividadesActivas();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    
    @GetMapping("/get_actividad_nombre")
    public List<ActividadDTO> obtenerActividadNombre(@RequestParam String nombre) {
        try {
            return actividadService.obtenerActividadesPorNombre(nombre);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public void crearActividad(@RequestBody ActividadDTO actividadDTO) {
        try {
            actividadService.guardarActividad(actividadDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/{id}/cupo_restante")
    public int cupoRestante(@PathVariable UUID id) {
        return actividadService.getCupoRestante(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    
    @GetMapping("/reiniciar_estado")
    public void reiniciarEstado() {
        activityCleanupService.deactivatePastActivities();
    }

    @PreAuthorize("hasRole('ADMIN')")
    
    @PostMapping("/listado_estudiante/{id_actividad}")
    public List<EstudianteDTO> listadoActividad(@PathVariable UUID id_actividad) {
        try {
            return actividadService.obtenerListadoEstudiante(id_actividad);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    
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
    
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable UUID id) {
        actividadService.eliminarActividad(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/resumen/{idActividad}")
    public ResumenActividadDTO getResumenActividad(@PathVariable UUID idActividad) {
        ActividadDTO actividad = actividadMapper.toDTO(actividadService.buscarPorId(idActividad));
        LugarDTO lugar = lugarService.buscarPorActividad(idActividad);
        ZonaAsistenciaDTO zona = null;

        if (asistenciaConfigStoreService.existeConfiguracion(idActividad)) {
            ConfigAsistenciaTemporal config = asistenciaConfigStoreService.obtenerConfiguracion(idActividad);
            zona = new ZonaAsistenciaDTO(idActividad, lugar.getLatitud(), config.getTiempoLimite());
        }

        return new ResumenActividadDTO(actividad, lugar, zona);
    }



}
