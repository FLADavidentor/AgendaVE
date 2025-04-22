package com.uam.agendave.controller;

import com.uam.agendave.dto.EstudianteDTO;
import com.uam.agendave.dto.LoginRequest;
import com.uam.agendave.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class LoginController {

    private final UsuarioService estudianteService;

    public LoginController(UsuarioService estudianteService) {
        this.estudianteService = estudianteService;
    }


    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> obtenerInformacionEstudiante(@RequestBody LoginRequest loginRequest) {
        try {


            EstudianteDTO estudianteData = estudianteService.loginUsuario(loginRequest);
            return ResponseEntity.ok().body(estudianteData);


        } catch (IllegalStateException e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));

        }catch (EntityNotFoundException e) {


            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {


            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(Map.of("error","Error interno del servidor"));
        }

    }
}
