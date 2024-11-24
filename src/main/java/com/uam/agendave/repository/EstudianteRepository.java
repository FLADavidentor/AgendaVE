package com.uam.agendave.repository;

import com.uam.agendave.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EstudianteRepository extends JpaRepository<Estudiante, UUID> {
    // Métodos personalizados pueden agregarse aquí
    List<Estudiante> findByNombreAndApellido(String nombre, String apellido);
    List<Estudiante> findByCif(String cif);
}
