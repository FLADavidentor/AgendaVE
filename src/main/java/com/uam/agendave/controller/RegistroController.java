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
    public List<Registro> obtenerTodos() {
        return registroService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Registro> obtenerPorId(@PathVariable UUID id) {
        Registro registro = registroService.buscarPorId(id);
        return new ResponseEntity<>(registro, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Registro> guardar(@RequestBody RegistroDTO registroDTO) {
        // Aquí podemos mapear el DTO a la entidad
        Registro registro = new Registro();
        // Aquí deberás mapear los campos del DTO al modelo de la entidad
        // registro.set...

        Registro nuevoRegistro = registroService.guardarRegistro(registro);
        return new ResponseEntity<>(nuevoRegistro, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        registroService.eliminarRegistro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public List<Registro> obtenerPorEstudiante(@PathVariable UUID idEstudiante) {
        return registroService.buscarPorEstudiante(idEstudiante);
    }

    @GetMapping("/actividad/{idActividad}")
    public List<Registro> obtenerPorActividad(@PathVariable UUID idActividad) {
        return registroService.buscarPorActividad(idActividad);
    }
}
