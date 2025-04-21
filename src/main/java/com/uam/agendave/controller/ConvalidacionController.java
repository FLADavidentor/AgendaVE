package com.uam.agendave.controller;

import com.uam.agendave.dto.ConvalidacionDTO;
import com.uam.agendave.service.ConvalidacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
//
//@RestController
//@RequestMapping("/convalidaciones")
//public class ConvalidacionController {
//
//    private final ConvalidacionService convalidacionService;
//
//    public ConvalidacionController(ConvalidacionService convalidacionService) {
//        this.convalidacionService = convalidacionService;
//    }
//
//    @GetMapping("/estudiante/{idEstudiante}")
//    public ResponseEntity<List<ConvalidacionDTO>> obtenerPorEstudiante(@PathVariable String idEstudiante) {
//        return ResponseEntity.ok(convalidacionService.obtenerPorEstudiante(idEstudiante));
//    }
//
//    @GetMapping("/actividad/{idActividad}")
//    public ResponseEntity<List<ConvalidacionDTO>> obtenerPorActividad(@PathVariable UUID idActividad) {
//        return ResponseEntity.ok(convalidacionService.obtenerPorActividad(idActividad));
//    }
//
//    @PostMapping("/convalidar/{idDetalleAsistencia}")
//    public ResponseEntity<Void> convalidar(@PathVariable UUID idDetalleAsistencia) {
//        convalidacionService.convalidarCreditos(idDetalleAsistencia);
//        return ResponseEntity.ok().build();
//    }
//
//}

