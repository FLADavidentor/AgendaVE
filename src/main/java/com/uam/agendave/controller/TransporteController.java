package com.uam.agendave.controller;

import com.uam.agendave.dto.Actividad.TransporteDTO;
import com.uam.agendave.service.Transporte.TransporteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transporte")
public class TransporteController {

    private final TransporteService transporteService;

    public TransporteController(TransporteService transporteService) {
        this.transporteService = transporteService;
    }

    // ======================== CREAR ========================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<TransporteDTO> crear(@RequestBody @Valid TransporteDTO transporteDTO) {
        TransporteDTO nuevo = transporteService.guardar(transporteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // ======================== OBTENER ========================

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/all")
    public List<TransporteDTO> obtenerTodos() {
        return transporteService.obtenerTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/{id}")
    public TransporteDTO obtenerPorId(@PathVariable UUID id) {
        return transporteService.buscarPorId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/lugar/{nombre}")
    public List<TransporteDTO> obtenerPorLugar(@PathVariable String nombre) {
        return transporteService.buscarPorLugar(nombre);
    }

    // ======================== ELIMINAR ========================

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        transporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}