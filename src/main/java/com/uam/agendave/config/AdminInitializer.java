package com.uam.agendave.config;

import com.uam.agendave.model.Usuario;
import com.uam.agendave.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner createDefaultAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            var correo = "admin@uam.edu.ni";

            if (usuarioRepository.findByCorreo(correo).isPresent()) {
                System.out.println("⚠️ Admin ya existe, no se crea nada.");
                return;
            }

            var admin = new Usuario();
            admin.setNombre("Alexander");
            admin.setApellido("Vado");
            admin.setUsername("AVADO");
            admin.setCorreo(correo);
            admin.setContrasena(passwordEncoder.encode("123")); // reemplazar por valor real u oculto en producción

            usuarioRepository.save(admin);
            System.out.println("✅ Admin inicial creado automáticamente al iniciar la app.");
        };
    }
}