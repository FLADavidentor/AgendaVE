package com.uam.agendave.service;


import com.uam.agendave.dto.UsuarioDTO;
import com.uam.agendave.model.Usuario;
import com.uam.agendave.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Usar BCrypt para codificar contraseñas
    }

    @Override
    public String autenticarUsuario(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con username: " + username));

        // Verificar la contraseña
        if (!passwordEncoder.matches(password, usuario.getContrasena())) {
            throw new IllegalArgumentException("Contraseña incorrecta para el usuario: " + username);
        }

        // Generar un token ficticio (puedes reemplazarlo con un token JWT real si lo necesitas)
        return "Bearer " + UUID.randomUUID();
    }



}
