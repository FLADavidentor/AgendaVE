package com.uam.agendave.controller;

import com.uam.agendave.dto.Actividad.LugarDTO;
import com.uam.agendave.service.lugar.LugarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController

@RequestMapping("/lugares")
public class LugarController {

    private final LugarService lugarService;

    public LugarController(LugarService lugarService) {
        this.lugarService = lugarService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    
    @GetMapping("/all")
    public ResponseEntity<List<LugarDTO>> obtenerTodos() {
        List<LugarDTO> lugares = lugarService.obtenerTodos();
        return ResponseEntity.ok(lugares);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/buscar")
    public ResponseEntity<List<LugarDTO>> buscarPorNombreParcial(@RequestParam String nombre) {
        List<LugarDTO> lugares = lugarService.buscarPorNombreParcial(nombre);
        return ResponseEntity.ok(lugares);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<LugarDTO> guardarLugar(@RequestBody LugarDTO lugarDTO) {
        LugarDTO lugarGuardado = lugarService.guardarLugar(lugarDTO);
        return ResponseEntity.ok(lugarGuardado);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/{id}")
    public ResponseEntity<LugarDTO> buscarPorId(@PathVariable UUID id) {
        LugarDTO lugarDTO = lugarService.buscarPorId(id);
        return ResponseEntity.ok(lugarDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarLugar(@PathVariable UUID id) {
        lugarService.eliminarLugar(id);
        return ResponseEntity.noContent().build();
    }


}
