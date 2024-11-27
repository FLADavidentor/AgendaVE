package com.uam.agendave.controller;

import com.uam.agendave.dto.DetalleAsistenciaDTO;
import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.DetalleAsistencia;
import com.uam.agendave.model.EstadoAsistencia;
import com.uam.agendave.service.DetalleAsistenciaService;
import com.uam.agendave.service.AsistenciaService;
import com.uam.agendave.service.RegistroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/detalle-asistencia")
public class DetalleAsistenciaController {

    private final DetalleAsistenciaService detalleAsistenciaService;
    private final AsistenciaService asistenciaService;
    private final RegistroService registroService;

    public DetalleAsistenciaController(DetalleAsistenciaService detalleAsistenciaService,
                                       AsistenciaService asistenciaService,
                                       RegistroService registroService) {
        this.detalleAsistenciaService = detalleAsistenciaService;
        this.asistenciaService = asistenciaService;
        this.registroService = registroService;
    }

    // Crear DetalleAsistencia
    @PostMapping
    public ResponseEntity<DetalleAsistencia> crearDetalleAsistencia(@RequestBody DetalleAsistenciaDTO detalleAsistenciaDTO) {
        EstadoAsistencia estadoAsistencia = EstadoAsistencia.valueOf(detalleAsistenciaDTO.getEstadoAsistencia().toUpperCase());

        if (asistenciaService.buscarPorId(detalleAsistenciaDTO.getIdAsistencia()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        RegistroDTO registroDTO = registroService.buscarPorId(detalleAsistenciaDTO.getIdRegistro());
        if (registroDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        DetalleAsistencia detalleAsistencia = new DetalleAsistencia();
        detalleAsistencia.setEstadoAsistencia(estadoAsistencia);
        detalleAsistencia.setAsistencia(asistenciaService.buscarPorId(detalleAsistenciaDTO.getIdAsistencia()));
        detalleAsistencia.setRegistro(registroService.convertirADetalleAsistencia(registroDTO)); // Conversión a Registro

        DetalleAsistencia savedDetalle = detalleAsistenciaService.guardarDetalleAsistencia(detalleAsistencia);
        return new ResponseEntity<>(savedDetalle, HttpStatus.CREATED);
    }


    // Obtener todos los DetalleAsistencia
    @GetMapping("/all")
    public ResponseEntity<List<DetalleAsistencia>> obtenerTodos() {
        List<DetalleAsistencia> detalles = detalleAsistenciaService.obtenerTodos();
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleAsistencia> obtenerPorId(@PathVariable UUID id) {
        DetalleAsistencia detalleAsistencia = detalleAsistenciaService.buscarPorId(id);
        return new ResponseEntity<>(detalleAsistencia, HttpStatus.OK);
    }

    // Eliminar DetalleAsistencia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleAsistencia(@PathVariable UUID id) {
        detalleAsistenciaService.eliminarDetalleAsistencia(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Obtener DetalleAsistencia por Asistencia ID
    @GetMapping("/asistencia/{asistenciaId}")
    public ResponseEntity<List<DetalleAsistencia>> obtenerPorAsistenciaId(@PathVariable UUID asistenciaId) {
        List<DetalleAsistencia> detalles = detalleAsistenciaService.obtenerPorAsistenciaId(asistenciaId);
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    // Obtener DetalleAsistencia por Registro ID
    @GetMapping("/registro/{registroId}")
    public ResponseEntity<List<DetalleAsistencia>> obtenerPorRegistroId(@PathVariable UUID registroId) {
        List<DetalleAsistencia> detalles = detalleAsistenciaService.obtenerPorRegistroId(registroId);
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }
}