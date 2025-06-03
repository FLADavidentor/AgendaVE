package com.uam.agendave.controller;

import com.uam.agendave.model.Usuario;
import com.uam.agendave.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @PostMapping("/crear")
    public ResponseEntity<String> crearAdmin() {
        var correo = "vadoalexander@gmail.com";

        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            return ResponseEntity.badRequest().body("ya existe, next");
        }

        var admin = new Usuario();
        admin.setNombre("Alexander");
        admin.setApellido("Vado");
        admin.setUsername("AVADO");
        admin.setCorreo(correo);
        admin.setContrasena(passwordEncoder.encode("123")); // 💀💀💀

        usuarioRepository.save(admin);

        return ResponseEntity.ok("admin dropped successfully 💀");
    }
}
