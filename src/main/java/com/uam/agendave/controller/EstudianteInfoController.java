package com.uam.agendave.controller;

import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.service.RegistroService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/estudiante_info")

public class EstudianteInfoController {

    private final RegistroService registroService;

    public EstudianteInfoController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    
    @PostMapping("/{cif}/creditos")
    public ResponseEntity<Map<TipoConvalidacion, Integer>> obtenerCreditosEstudiante(@PathVariable String cif) {
        Map<TipoConvalidacion, Integer> totales =
                registroService.obtenerTotalCreditosPorTipo(cif);
        System.out.println(totales);
        return ResponseEntity.ok(totales);
    }
}
