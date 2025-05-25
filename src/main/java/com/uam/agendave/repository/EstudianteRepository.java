package com.uam.agendave.repository;

import com.uam.agendave.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstudianteRepository extends JpaRepository<Estudiante, UUID> {

    Optional<Estudiante> findByCif(String cif);
    Optional<List<Estudiante>> findByCifIn(List<String> cifs);

}
