package com.uam.agendave.repository;

import com.uam.agendave.model.Convalidacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConvalidacionRepository extends JpaRepository<Convalidacion, UUID> {
    List<Convalidacion> findByEstudianteId(UUID estudianteId);
    List<Convalidacion> findByActividadId(UUID actividadId);
}
