package com.uam.agendave.repository;

import com.uam.agendave.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EstudianteRepository extends JpaRepository<Estudiante, UUID> {

    Optional findByCif(String cif);

}
