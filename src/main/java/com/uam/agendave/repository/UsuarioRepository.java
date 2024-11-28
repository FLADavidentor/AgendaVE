package com.uam.agendave.repository;

import com.uam.agendave.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Agrega métodos personalizados si son necesarios
    List<Usuario> findByNombreContainingIgnoreCase(String nombre); // Búsqueda por nombre parcial
    Optional<Usuario> findByCorreoIgnoreCase(String correo);      // Búsqueda exacta por correo
    Optional<Usuario> findByUsername(String username);
}
