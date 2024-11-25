package com.uam.agendave.controller;

import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Registro;
import com.uam.agendave.service.RegistroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping
    public List<RegistroDTO> obtenerTodos() {
        return registroService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroDTO> obtenerPorId(@PathVariable UUID id) {
        RegistroDTO registro = registroService.buscarPorId(id);
        return new ResponseEntity<>(registro, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RegistroDTO> guardar(@RequestBody RegistroDTO registroDTO) {
        RegistroDTO nuevoRegistro = registroService.guardarRegistro(registroDTO);
        return new ResponseEntity<>(nuevoRegistro, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        registroService.eliminarRegistro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public List<RegistroDTO> obtenerPorEstudiante(@PathVariable UUID idEstudiante) {
        return registroService.buscarPorEstudiante(idEstudiante);
    }

    @GetMapping("/actividad/{idActividad}")
    public List<RegistroDTO> obtenerPorActividad(@PathVariable UUID idActividad) {
        return registroService.buscarPorActividad(idActividad);
    }
}

