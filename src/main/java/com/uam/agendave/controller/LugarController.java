package com.uam.agendave.controller;

import com.uam.agendave.dto.LugarDTO;
import com.uam.agendave.service.LugarService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<LugarDTO>> obtenerTodos() {
        List<LugarDTO> lugares = lugarService.obtenerTodos();
        return ResponseEntity.ok(lugares);
    }

    @PostMapping
    public ResponseEntity<LugarDTO> guardarLugar(@RequestBody LugarDTO lugarDTO) {
        LugarDTO lugarGuardado = lugarService.guardarLugar(lugarDTO);
        return ResponseEntity.ok(lugarGuardado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LugarDTO> buscarPorId(@PathVariable UUID id) {
        LugarDTO lugarDTO = lugarService.buscarPorId(id);
        return ResponseEntity.ok(lugarDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLugar(@PathVariable UUID id) {
        lugarService.eliminarLugar(id);
        return ResponseEntity.noContent().build();
    }
}
